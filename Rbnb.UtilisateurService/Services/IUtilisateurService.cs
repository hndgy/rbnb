

using Rbnb.UtilisateuService.Dtos;
using Rbnb.UtilisateuService.Entities;

namespace Rbnb.UtilisateuService.Services;
public interface IUtilisateurService
{
    Task<Utilisateur> InscrireUtilisateurAsync(CreationUtilisateurDto dto);
    Utilisateur GetUtilisateurById(string id);
    IEnumerable<Utilisateur> GetAllUtilisateur();

    void UpdateUtilisateur(string id, UtilisateurUpdateDto utilisateur);
    void RemoveUtilisateur( string id);

    String testPublish();
}