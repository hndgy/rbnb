using System.Runtime.Serialization;

namespace Rbnb.UtilisateuService.Services
{
    [Serializable]
    internal class UtilisateurNonTrouveException : Exception
    {
        public UtilisateurNonTrouveException() : base("Impossible de trouvé cet utilisateur.")
        {
        }


    }
}