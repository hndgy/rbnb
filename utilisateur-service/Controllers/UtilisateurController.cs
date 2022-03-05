using System.Net.Http.Headers;
using Microsoft.AspNetCore.Mvc;
using Newtonsoft.Json;
using utilisateur_service.Dtos;
using utilisateur_service.Entities;
using utilisateur_service.Models;

namespace utilisateur_service.Controllers;

[ApiController]
[Route("[controller]")]
public class UtilisateurController : ControllerBase
{
    private KeycloakClient _keycloakClient;
    private readonly ILogger<UtilisateurController> _logger;
    private HttpClient _httpClient;
    private UserServiceDbContext dbContext;

    public UtilisateurController(ILogger<UtilisateurController> logger)
    {
        _logger = logger;
        _keycloakClient = new KeycloakClient(
            host: "http://localhost:8080",
            realm: "test"
        );

        dbContext = new UserServiceDbContext();

        _httpClient = new HttpClient();
    }

    [HttpGet(Name = "GetAllUser")]
    public IEnumerable<Utilisateur> Get()
    {
        //return new List<Utilisateur>();
        return dbContext.Utilisateurs.AsEnumerable();
    }

    [HttpPost()]
    public async Task<IActionResult> Inscrire(CreationUtilisateurDto dto)
    {
        if (!dto.CheckPassword())
        {
            return BadRequest("Les mots de passe ne sont pas les mêmes");
        }
        var idCreated = await _keycloakClient.CreateUser(dto);

        if (idCreated != null)
        {
            Utilisateur utilisateur = Mapper.ConvertCreationUtilisateurDtoToUtilisateur(dto);
            utilisateur.Id = idCreated;
            dbContext.Utilisateurs.Add(utilisateur);
            dbContext.SaveChanges();
            return Ok(utilisateur);
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
    public async Task<IActionResult> GetToken()
    {
        var token = await _keycloakClient.GetTokenAsync("admin-cli", "admin", "admin");

        return Ok(token);
    }




}


