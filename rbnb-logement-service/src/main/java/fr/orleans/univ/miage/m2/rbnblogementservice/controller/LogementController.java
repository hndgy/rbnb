package fr.orleans.univ.miage.m2.rbnblogementservice.controller;

import fr.orleans.univ.miage.m2.rbnblogementservice.configuration.MQConfig;
import fr.orleans.univ.miage.m2.rbnblogementservice.dto.CreationLogementDto;
import fr.orleans.univ.miage.m2.rbnblogementservice.dto.LogementDto;
import fr.orleans.univ.miage.m2.rbnblogementservice.entity.Categorie;
import fr.orleans.univ.miage.m2.rbnblogementservice.entity.Equipement;
import fr.orleans.univ.miage.m2.rbnblogementservice.entity.Logement;
import fr.orleans.univ.miage.m2.rbnblogementservice.exception.LogementNotFoundException;
import fr.orleans.univ.miage.m2.rbnblogementservice.service.LogementService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.security.Principal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/logement")
public class LogementController {

    LogementService logementService;

    private final RabbitTemplate template;

    public LogementController(
            LogementService logementService,
            RabbitTemplate template
    ) {
        this.logementService = logementService;
        this.template = template;
    }

    @RolesAllowed({"ADMIN","USER"})
    @GetMapping
    public ResponseEntity<List<Logement>> getAllLogement(){
        List<Logement> logementList = null;
        logementList = logementService.getAllLogements();
        template.convertAndSend(MQConfig.LOGEMENT_EXCHANGE, MQConfig.LOGEMENT_ROUTING_KEY, logementList);
        return new ResponseEntity<>(logementList, new HttpHeaders(), HttpStatus.OK);
    }

    @RolesAllowed({"ADMIN","USER"})
    @GetMapping("/search")
    public ResponseEntity<List<Logement>> getAllLogementByCityAndNbVoyageurs(
            @RequestParam(defaultValue = "paris") String city,
            @RequestParam(defaultValue = "1") int voyageurs) throws LogementNotFoundException
    {
        List<Logement> logementList = null;
        try {
//            logementList = logementService.findAllLogementByCity(city.toLowerCase());
            logementList = logementService.findLogementsByCityAndNbVoyageurs(city.toLowerCase(), voyageurs);
            template.convertAndSend(MQConfig.LOGEMENT_EXCHANGE, MQConfig.LOGEMENT_ROUTING_KEY, logementList);
            return new ResponseEntity<>(logementList, new HttpHeaders(), HttpStatus.OK);
        } catch (LogementNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

/*
//    @RolesAllowed({"ADMIN","USER"})
    @GetMapping
    public String getAllLogement(){
        List<Logement> logementList = null;
        logementList = logementService.getAllLogements();
        template.convertAndSend(MQConfig.EXCHANGE, MQConfig.ROUTING_KEY, logementList);
        return "Message envoyé";
    }

 */

    @RolesAllowed("HOTE")
    @PostMapping
    public ResponseEntity<Logement> createLogement(
            Principal principal,
            @RequestBody CreationLogementDto creationLogementDto
    )
    {
        String idProprietaire = principal.getName();
        List<Equipement> equipements = creationLogementDto
                .idEquipements()
                .stream()
                .map(
                        id -> logementService.getEquipementById(id)
                ).collect(Collectors.toList());
        List<Categorie> categories = creationLogementDto
                .idCategories()
                .stream()
                .map(
                        id -> logementService.getCategorieById(id)
                ).collect(Collectors.toList());

        Logement logement = new Logement();
        logement.setIdProprietaire(idProprietaire);
        logement.setLibelle(creationLogementDto.libelle());
        logement.setAddress(creationLogementDto.address());
        logement.setCity(creationLogementDto.city().toLowerCase());
        logement.setNbVoyageurs(creationLogementDto.nbVoyageurs());
        logement.setEquipements(equipements);
        logement.setCategories(categories);

        Logement nouveauLogement = logementService.createOrUpdateLogement(logement);
        return new ResponseEntity<>(nouveauLogement, new HttpHeaders(), HttpStatus.CREATED);
    }


    @RolesAllowed({"HOTE","USER"})
    @GetMapping("/{idLogement}")
    public ResponseEntity<LogementDto> getLogement(
            @PathVariable Long idLogement,
            @RequestHeader(name = "Authorization") String token
    ){
        LogementDto logement = null;
        try {
            logement = logementService.getLogementDetailById(idLogement
//                    , token
            );
            template.convertAndSend(MQConfig.LOGEMENT_EXCHANGE, MQConfig.LOGEMENT_ROUTING_KEY, logement);

            return new ResponseEntity<>(logement, new HttpHeaders(), HttpStatus.OK);
        } catch (LogementNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

/*
    @RolesAllowed({"HOTE","USER"})
    @GetMapping("/{idLogement}")
    public ResponseEntity<LogementDto> getLogement(@PathVariable Long idLogement){

        LogementDto logement = null;
        //@TODO récupérer le nom et prénom du proprietaire et les renvoyer via le dto
        try {
            //@TODO récupérer les infos ici depuis le service utilisateur et les ajouter à l'entité
            UtilisateurDto utilisateurDto = new UtilisateurDto();


            logement = logementService.getLogementDetailById(idLogement);
            LogementDto logementDto = new LogementDto(
                    logement.libelle(), logement.address(), logement.city(), logement.nbVoyageurs(),
                    utilisateurDto, logement.images(), logement.equipements(),
                    logement.categories());
            template.convertAndSend(MQConfig.LOGEMENT_EXCHANGE, MQConfig.LOGEMENT_ROUTING_KEY, logementDto);


            return new ResponseEntity<>(logementDto, new HttpHeaders(), HttpStatus.OK);
        } catch (LogementNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    */

    @RolesAllowed("HOTE")
    @DeleteMapping("/{idLogement}")
    public ResponseEntity<Object> deleteLogement(Principal principal, @PathVariable Long idLogement)
            throws LogementNotFoundException {
        Optional<Logement> logement = logementService.getLogementById(idLogement);
        String idProprietaire = logement.get().getIdProprietaire();
        if (Objects.equals(principal.getName(), idProprietaire))
        {
            try {
                logementService.deleteLogement(idLogement);
                template.convertAndSend(
                        MQConfig.LOGEMENT_EXCHANGE,
                        MQConfig.LOGEMENT_ROUTING_KEY,
                        "Le logement a bien été supprimé !\n" +
                                "Données du logement supprimé :\n" + logement);
                return ResponseEntity.status(HttpStatus.OK).build();
            } catch (LogementNotFoundException e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        }
        else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }


    @RolesAllowed("HOTE")
    @PutMapping("/{idLogement}")
    public ResponseEntity<Object> updateLogement(
            Principal principal,
            @PathVariable Long idLogement,
            @RequestBody CreationLogementDto creationLogementDto
    ) throws LogementNotFoundException
    {
        Optional<Logement> logement = logementService.getLogementById(idLogement);
        String idProprietaire = logement.get().getIdProprietaire();
        if (Objects.equals(principal.getName(), idProprietaire))
        {
            List<Equipement> equipements = creationLogementDto
                    .idEquipements()
                    .stream()
                    .map(
                            id -> logementService.getEquipementById(id)
                    ).collect(Collectors.toList());
            List<Categorie> categories = creationLogementDto
                    .idCategories()
                    .stream()
                    .map(
                            id -> logementService.getCategorieById(id)
                    ).collect(Collectors.toList());

            Logement updateLogement = new Logement();
            updateLogement.setId(idLogement);
            if (creationLogementDto.libelle() != null) {
                updateLogement.setLibelle(creationLogementDto.libelle());
            }
            if (creationLogementDto.address() != null) {
                updateLogement.setAddress(creationLogementDto.address());
            }
            if (creationLogementDto.city() != null) {
                updateLogement.setCity(creationLogementDto.city());
            }
            if (creationLogementDto.nbVoyageurs() != 0) {
                updateLogement.setNbVoyageurs(creationLogementDto.nbVoyageurs());
            }
            updateLogement.setIdProprietaire(idProprietaire);
            updateLogement.setEquipements(equipements);
            updateLogement.setCategories(categories);
            logementService.createOrUpdateLogement(updateLogement);
            return new ResponseEntity<>(updateLogement, new HttpHeaders(), HttpStatus.OK);
        }
        else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @RolesAllowed({"ADMIN","HOTE"})
    @GetMapping("/proprietaire/{idProprietaire}")
    public ResponseEntity<List<Logement>> getLogementsByProprietaire(@PathVariable String idProprietaire){
        List<Logement> logements = null;
        try {
            logements = logementService.getAllLogementsByIdProprietaire(idProprietaire);

            template.convertAndSend(MQConfig.LOGEMENT_EXCHANGE, MQConfig.LOGEMENT_ROUTING_KEY, logements);
            return new ResponseEntity<>(logements, new HttpHeaders(), HttpStatus.OK);
        } catch (LogementNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }



}
