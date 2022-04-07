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


    [HttpPut]
    [Authorize(Roles = "USER,HOTE")]
    [Route("{id}")]
    public IActionResult Update([FromRoute] String id, Utilisateur utilisateur)
    {
        foreach(var c in HttpContext.User.Claims){
            System.Console.WriteLine(c.Value);
        }
        try{
            _utilisateurService.UpdateUtilisateur(id,utilisateur);
            return Ok();
        }catch(Exception e){
            return BadRequest(e.Message);
        }
       
    }

    [HttpDelete]
    [Authorize(Roles = "USER,HOTE,ADMIN")]
    [Route("{id}")]
    public IActionResult Delete([FromRoute]string id)
    {
        foreach(var c in HttpContext.User.Claims){
            System.Console.WriteLine(c.Value);
        }
        try{
            _utilisateurService.RemoveUtilisateur(id);
            return NoContent();
        }catch(Exception e){
            return BadRequest(e.Message);
        }
       
    }


    [AllowAnonymous]
    [HttpGet]
    [Route("test")]
    public IActionResult Test(){
        return Ok(_utilisateurService.testPublish());
    }


}


