using System.Configuration;
using Rbnb.UtilisateuService.Dtos;
using Rbnb.UtilisateuService.Entities;
using Rbnb.UtilisateuService.Models;

namespace Rbnb.UtilisateuService.Services;

public class UtilisateurService : IUtilisateurService
{
    public KeycloakClient _keycloakClient { get; private set; }
    public UserServiceDbContext dbContext { get; private set; }

    public UtilisateurService(IConfiguration config)
    {

        _keycloakClient = new KeycloakClient(config);

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