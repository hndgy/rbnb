using utilisateur_service.Dtos;
using utilisateur_service.Entities;

namespace utilisateur_service.Models;
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