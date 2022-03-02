using Keycloak.Net;
using Microsoft.AspNetCore.Mvc;
using utilisateur_service.Entities;
using utilisateur_service.Models;

namespace utilisateur_service.Controllers;

[ApiController]
[Route("[controller]")]
public class UtilisateurController : ControllerBase
{
    private readonly ILogger<UtilisateurController> _logger;
    private readonly KeycloakClient _keycloakClient;
    private UserServiceDbContext dbContext;

    public UtilisateurController(ILogger<UtilisateurController> logger)
    {
        _logger = logger;
        _keycloakClient = new KeycloakClient(
            url: "http://localhost:8080",
            userName: "admin",
            password: "admin"
        );
        //instancier dbContext
    }

    [HttpGet(Name = "GetAllUser")]
    public IEnumerable<Utilisateur> Get()
    {
        return dbContext.Utilisateurs.AsEnumerable();
    }

    [HttpPost()]
    public Utilisateur Inscrire(Utilisateur utilisateur)
    {
        var created = _keycloakClient.CreateAndRetrieveUserIdAsync(
            realm: "test",
            user: new Keycloak.Net.Models.Users.User()
            {
                UserName = utilisateur.Nom,
                LastName = utilisateur.Nom,
                FirstName = utilisateur.Prenom,
                Email = utilisateur.Mail
            }
        );
        created.Wait();
        var res = created.Result;
        if (res != null)
        {
            utilisateur.Id = res;
            dbContext.Utilisateurs.Add(utilisateur);
            return utilisateur;


        }
        return null;
    }


}
