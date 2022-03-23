using System.Runtime.Serialization;

namespace utilisateur_service.Services
{
    [Serializable]
    internal class UtilisateurNonCreeException : Exception
    {
        public UtilisateurNonCreeException() : base("Impossible de créé l'utilisateur. Veuillez Rééssayer.")
        {
        }

    }
}