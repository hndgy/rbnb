using Rbnb.UtilisateuService.Dtos;
using Rbnb.UtilisateuService.Entities;

namespace Rbnb.UtilisateuService.Models;
public class Mapper
{
    public static Utilisateur ConvertCreationUtilisateurDtoToUtilisateur(CreationUtilisateurDto obj)
    {
        return new Utilisateur()
        {
            Nom = obj.Nom,
            Prenom = obj.Prenom,
            UserName = obj.Username,
            Mail = obj.Mail
        };
    }
}