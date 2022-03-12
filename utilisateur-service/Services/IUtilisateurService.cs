using utilisateur_service.Dtos;
using utilisateur_service.Entities;

namespace utilisateur_service.Services;
public interface IUtilisateurService
{
    Task<Utilisateur> InscrireUtilisateurAsync(CreationUtilisateurDto dto);
    Utilisateur GetUtilisateurById(string id);
    IEnumerable<Utilisateur> GetAllUtilisateur();
}