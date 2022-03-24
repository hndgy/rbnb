using System.Net.Http.Headers;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Newtonsoft.Json;
using Rbnb.UtilisateuService.Dtos;
using Rbnb.UtilisateuService.Entities;
using Rbnb.UtilisateuService.Models;
using Rbnb.UtilisateuService.Services;

namespace Rbnb.UtilisateuService.Controllers;

[ApiController]
[Route("[controller]")]
public class UtilisateurController : ControllerBase
{
    private IUtilisateurService _utilisateurService;
    private readonly ILogger<UtilisateurController> _logger;

    public UtilisateurController(ILogger<UtilisateurController> logger, IUtilisateurService utilisateurService)
    {
        _logger = logger;
        _utilisateurService = utilisateurService;
    }

    [Authorize(Roles = "ADMIN")]
    [HttpGet(Name = "GetAllUser")]
    public IEnumerable<Utilisateur> Get()
    {
        return _utilisateurService.GetAllUtilisateur();
    }

    [AllowAnonymous]
    [HttpPost()]
    public async Task<IActionResult> Inscrire(CreationUtilisateurDto dto)
    {
        try
        {
            var user = await _utilisateurService.InscrireUtilisateurAsync(dto);
            return Created("", user);
        }
        catch (Exception e)
        {
            System.Console.WriteLine(e);
            return BadRequest(e.Message);
        }
    }

    [HttpGet]
    [Authorize(Roles = "USER,HOTE")]
    [Route("{id}")]
    public IActionResult GetById([FromRoute] String id)
    {

        try
        {
            var user = _utilisateurService.GetUtilisateurById(id);
            return Ok(user);
        }
        catch (Exception e)
        {
            return NotFound(e.Message);
        }
    }


}


