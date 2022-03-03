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

        dbContext = new UserServiceDbContext();
    }

    [HttpGet(Name = "GetAllUser")]
    public IEnumerable<Utilisateur> Get()
    {
        //return new List<Utilisateur>();
        return dbContext.Utilisateurs.AsEnumerable();
    }

    [HttpPost()]
    public Utilisateur Inscrire(Utilisateur utilisateur)
    {
        /*var created = _keycloakClient.CreateAndRetrieveUserIdAsync(
        realm: "master",
        user: new Keycloak.Net.Models.Users.User()
        {
            UserName = utilisateur.Nom,
            LastName = utilisateur.Nom,
            FirstName = utilisateur.Prenom,
            Email = utilisateur.Mail
        });
        created.Wait();
        var res = created.Result;*/
        if (true)
        {
            utilisateur.Id = Guid.NewGuid().ToString();
            dbContext.Utilisateurs.Add(utilisateur);
            dbContext.SaveChanges();
            return utilisateur;


        }
        return null;
    }

    [HttpGet]
    [Route("{id}")]
    public IActionResult GetById([FromRoute] String id)
    {

        var user = dbContext.Utilisateurs.Where(u => u.Id == id).FirstOrDefault();
        if (user == null)
        {
            return NotFound();
        }
        return Ok(user);
    }

    [HttpGet]
    [Route("test")]
    public IActionResult Test()
    {

        var resp = _keycloakClient.CreateInitialAccessTokenAsync(
            "master",
            new Keycloak.Net.Models.ClientInitialAccess.ClientInitialAccessCreatePresentation()
            );
        resp.Wait();

        /* var created = _keycloakClient.CreateAndRetrieveUserIdAsync(
         realm: "master",
         user: new Keycloak.Net.Models.Users.User()
         {
             UserName = Guid.NewGuid().ToString(),
             LastName = "test",
             FirstName = "test",
             Email = "test@tes.fr"
         });
         created.Wait();$*/
        return Ok(resp.Result);
    }

}
