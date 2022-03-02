using Keycloak.Net;
using Microsoft.AspNetCore.Mvc;
using utilisateur_service.Entities;

namespace utilisateur_service.Controllers;

[ApiController]
[Route("[controller]")]
public class UtilisateurController : ControllerBase
{
    private readonly ILogger<UtilisateurController> _logger;
    private readonly KeycloakClient _keycloakClient;
    private List<Utilisateur> _list;

    public UtilisateurController(ILogger<UtilisateurController> logger)
    {
        _logger = logger;
        _keycloakClient = new KeycloakClient(
            url: "http://localhost:8080",
            userName: "admin",
            password: "admin"
        );
        _list = new List<Utilisateur>();
    }

    [HttpGet(Name = "GetAllUser")]
    public IEnumerable<Utilisateur> Get()
    {
        return _list;
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
            _list.Add(utilisateur);
            return utilisateur;


        }
        return null;
    }


}
