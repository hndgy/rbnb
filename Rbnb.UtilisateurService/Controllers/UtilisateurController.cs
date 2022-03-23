using System.Net.Http.Headers;
using Microsoft.AspNetCore.Mvc;
using Newtonsoft.Json;
using Rbnb.UtilisateuService.Dtos;
using Rbnb.UtilisateuService.Entities;
using Rbnb.UtilisateuService.Models;

namespace Rbnb.UtilisateuService.Controllers;

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
        var utilisateurKeycloakResponse = await _keycloakClient.CreateUser(dto);

        if (utilisateurKeycloakResponse.IsSuccessStatusCode)
        {
            Utilisateur utilisateur = Mapper.ConvertCreationUtilisateurDtoToUtilisateur(dto);
            utilisateur.Id = utilisateurKeycloakResponse.UserId;
            dbContext.Utilisateurs.Add(utilisateur);
            dbContext.SaveChanges();
            return Ok(utilisateur);
        }
        return BadRequest();
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




}


