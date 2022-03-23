using System.Runtime.Serialization;

namespace utilisateur_service.Services
{
    [Serializable]
    internal class UtilisateurNonTrouveException : Exception
    {
        public UtilisateurNonTrouveException() : base("Impossible de trouvé cet utilisateur.")
        {
        }


    }
}