namespace Rbnb.UtilisateuService.Dtos;
public class CreationUtilisateurDto
{
    public String? Nom { get; set; }
    public String? Prenom { get; set; }
    public String? Mail { get; set; }
    public String? Username { get; set; }
    public String? Password { get; set; }
    public String? PasswordConfirmation { get; set; }

    public bool CheckPassword() => Password == PasswordConfirmation;
}