using System.Configuration;
using utilisateur_service.Dtos;
using utilisateur_service.Entities;
using utilisateur_service.Models;

namespace utilisateur_service.Services;

public class UtilisateurService : IUtilisateurService
{
    public KeycloakClient _keycloakClient { get; private set; }
    public UserServiceDbContext dbContext { get; private set; }

    public UtilisateurService()
    {
        var appSettings = System.Configuration.ConfigurationManager.AppSettings;
        var kcHost = appSettings["keycloak:host"];
        var kcRealm = appSettings["keycloak:realm"];
        if (kcHost == null || kcRealm == null)
        {
            throw new Exception("Veuillez saisir les champs host et realms dans le ficher appSettings.cs");
        }
        _keycloakClient = new KeycloakClient(
           host: kcHost,
           realm: kcRealm
       );

        dbContext = new UserServiceDbContext();
    }

    public async Task<Utilisateur> InscrireUtilisateurAsync(CreationUtilisateurDto dto)
    {
        if (!dto.CheckPassword())
        {
            throw new InformationsIncorrectesException();
        }

        var utilisateurKeycloakResponse = await _keycloakClient.CreateUser(dto);

        if (utilisateurKeycloakResponse.IsSuccessStatusCode)
        {
            Utilisateur utilisateur = Mapper.ConvertCreationUtilisateurDtoToUtilisateur(dto);
            utilisateur.Id = utilisateurKeycloakResponse.UserId;
            dbContext.Utilisateurs.Add(utilisateur);
            dbContext.SaveChanges();
            return utilisateur;
        }
        throw new UtilisateurNonCreeException();

    }

    public Utilisateur GetUtilisateurById(string id)
    {
        var user = dbContext.Utilisateurs.Where(u => u.Id == id).FirstOrDefault();
        if (user == null)
        {
            throw new UtilisateurNonTrouveException();
        }
        return user;
    }

    public IEnumerable<Utilisateur> GetAllUtilisateur()
    {
        return dbContext.Utilisateurs.AsEnumerable();
    }
}