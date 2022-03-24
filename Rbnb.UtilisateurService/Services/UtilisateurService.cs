using System.Configuration;
using Rbnb.UtilisateuService.Dtos;
using Rbnb.UtilisateuService.Entities;
using Rbnb.UtilisateuService.Models;

namespace Rbnb.UtilisateuService.Services;

public class UtilisateurService : IUtilisateurService
{
    public KeycloakClient _keycloakClient { get; private set; }
    public UserServiceDbContext _dbContext { get; private set; }

    public UtilisateurService(IConfiguration config, UserServiceDbContext dbContext)
    {

        _keycloakClient = new KeycloakClient(config);

        _dbContext = dbContext;
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
            _dbContext.Utilisateurs.Add(utilisateur);
            _dbContext.SaveChanges();
            return utilisateur;
        }
        throw new UtilisateurNonCreeException();

    }

    public Utilisateur GetUtilisateurById(string id)
    {
        var user = _dbContext.Utilisateurs.Where(u => u.Id == id).FirstOrDefault();
        if (user == null)
        {
            throw new UtilisateurNonTrouveException();
        }
        return user;
    }

    public IEnumerable<Utilisateur> GetAllUtilisateur()
    {
        return _dbContext.Utilisateurs.AsEnumerable();
    }
}