using System.Runtime.Serialization;

namespace utilisateur_service.Services
{
    [Serializable]
    internal class InformationsIncorrectesException : Exception
    {
        public InformationsIncorrectesException() : base("Les informations ne sont pas correctes.")
        {
        }

    }
}