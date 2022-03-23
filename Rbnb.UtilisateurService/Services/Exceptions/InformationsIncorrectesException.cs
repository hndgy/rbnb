using System.Runtime.Serialization;

namespace Rbnb.UtilisateuService.Services
{
    [Serializable]
    internal class InformationsIncorrectesException : Exception
    {
        public InformationsIncorrectesException() : base("Les informations ne sont pas correctes.")
        {
        }

    }
}