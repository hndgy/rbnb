package fr.orleans.univ.miage.m2.rbnblogementservice.service;

import fr.orleans.univ.miage.m2.rbnblogementservice.dto.LogementDto;
import fr.orleans.univ.miage.m2.rbnblogementservice.dto.UtilisateurDto;
import fr.orleans.univ.miage.m2.rbnblogementservice.entity.Categorie;
import fr.orleans.univ.miage.m2.rbnblogementservice.entity.Equipement;
import fr.orleans.univ.miage.m2.rbnblogementservice.entity.Logement;
import fr.orleans.univ.miage.m2.rbnblogementservice.exception.LogementNotFoundException;
import fr.orleans.univ.miage.m2.rbnblogementservice.repository.CategorieRepository;
import fr.orleans.univ.miage.m2.rbnblogementservice.repository.EquipementRepository;
import fr.orleans.univ.miage.m2.rbnblogementservice.repository.LogementRepository;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class LogementServiceImpl implements LogementService {

    private final LogementRepository logementRepository;
    private final EquipementRepository equipementRepository;
    private final CategorieRepository categorieRepository;
    private final RestTemplate restTemplate;


    public LogementServiceImpl(
            LogementRepository logementRepository,
            EquipementRepository equipementRepository,
            CategorieRepository categorieRepository,
            RestTemplate restTemplate)
    {
        this.logementRepository = logementRepository;
        this.equipementRepository = equipementRepository;
        this.categorieRepository = categorieRepository;
        this.restTemplate = restTemplate;
    }

    @Override
    public Logement createOrUpdateLogement(Logement entity) {
        if (entity.getId() != null) {
            Optional<Logement> logement = logementRepository.findById(entity.getId());
            if (logement.isPresent()) {
                Logement nouveauLogement = logement.get();
                nouveauLogement.setLibelle(entity.getLibelle());
                nouveauLogement.setAddress(entity.getAddress());
                nouveauLogement.setIdProprietaire(entity.getIdProprietaire());
                nouveauLogement.setCategories(entity.getCategories());
                nouveauLogement.setEquipements(entity.getEquipements());

                nouveauLogement = logementRepository.save(nouveauLogement);
                return nouveauLogement;
            }
            else {
                entity = logementRepository.save(entity);
                return entity;
            }
        }
        else {
            entity = logementRepository.save(entity);
            return entity;
        }

    }

    @Override
    public Optional<Logement> getLogementById(Long idLogement) throws LogementNotFoundException {
        Optional<Logement> logement = logementRepository.findById(idLogement);
        if (logement.isPresent()){
            return logementRepository.findById(idLogement);
        }
        else {
            throw new LogementNotFoundException("Logement introuvable pour l'id : " + idLogement);
        }
    }

    @Override
    public LogementDto getLogementDetailById(Long idLogement, String token) throws LogementNotFoundException {
        Optional<Logement> logement = logementRepository.findById(idLogement);
        if (logement.isPresent()){
            Logement logement1 = logementRepository.findById(idLogement).get();
            String idUtilisateur = logement1.getIdProprietaire();
            //@TODO requête vers le service utilisateur pour récupérer les infos utilisateur
            HttpHeaders headers = new HttpHeaders();
            String[] tokenArray = token.split(" ");
            headers.set("Accept", "application/json");
            headers.add("Authorization", "Bearer " + tokenArray[1]);
            HttpEntity<String> entity = new HttpEntity<>(headers);
            String urlUtilisateur = "http://localhost:9002/Utilisateur/"+ idUtilisateur;
            UtilisateurDto restUtilisateurDto = restTemplate.exchange(urlUtilisateur, HttpMethod.GET, entity, UtilisateurDto.class).getBody();

//            String prenom = "benoit"; //requete prenom
//            String nom = "hote"; //requete nom

//            UtilisateurDto utilisateurDto = new UtilisateurDto();
//            utilisateurDto.setId(idUtilisateur);
//            utilisateurDto.setPrenom(prenom);
//            utilisateurDto.setNom(nom);

            LogementDto logementDto = new LogementDto(
                    logement1.getLibelle(),
                    logement1.getAddress(),
                    logement1.getNbVoyageurs(),
                    restUtilisateurDto,
//                    utilisateurDto,
                    logement1.getImages(),
                    logement1.getEquipements(),
                    logement1.getCategories()
            );
            return logementDto;
        }
        else {
            throw new LogementNotFoundException("Logement introuvable pour l'id : " + idLogement);
        }
    }

    @Override
    public List<Logement> getAllLogementsByIdProprietaire(String idProprietaire) throws LogementNotFoundException {
        List<Logement> logements = logementRepository.findLogementsByIdProprietaire(idProprietaire);
        if (logements.isEmpty()) {
            throw new LogementNotFoundException("Logements introuvables pour le propriétaire : " + idProprietaire);
        }
        else return logements;
    }

//
//    @Override
//    public Optional<Logement> getLogementByIdProprietaireAndId(String proprietaire, Long idLogement) throws LogementNotFoundException {
//        Optional<Logement> logement = logementRepository.findLogementByIdProprietaireAndId(proprietaire, idLogement);
//        if (logement.isPresent()) {
//            return logement;
//        }
//        else throw new LogementNotFoundException("Logements introuvables pour le propriétaire : " + proprietaire);
//    }

    @Override
    public List<Logement> findAllLogementByAddress(String address) throws LogementNotFoundException {
        List<Logement> logements = logementRepository.findLogementsByAddress(address);
        if (logements.isEmpty()) {
            throw new LogementNotFoundException("Logements introuvables pour l'adresse : " + address);
        }
        else return logements;
    }

    @Override
    public List<Logement> getAllLogements() {
        return logementRepository.findAll();
    }

    @Override
    public void deleteLogement(Long id) throws LogementNotFoundException {
        Optional<Logement> logement = logementRepository.findById(id);

        if (logement.isPresent()) {
            logementRepository.deleteById(id);
        }
        else {
            throw new LogementNotFoundException("Logement introuvable pour l'id : " + id);
        }
    }

    @Override
    public Equipement getEquipementById(Long id) {
        return equipementRepository.findEquipementByIdEquipement(id);
    }

    @Override
    public Categorie getCategorieById(Long id) {
        return categorieRepository.findCategorieByIdCategorie(id);
    }
}
