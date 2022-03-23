using System.Runtime.Serialization;

namespace Rbnb.UtilisateuService.Services
{
    [Serializable]
    internal class UtilisateurNonCreeException : Exception
    {
        public UtilisateurNonCreeException() : base("Impossible de créé l'utilisateur. Veuillez Rééssayer.")
        {
        }

    }
}