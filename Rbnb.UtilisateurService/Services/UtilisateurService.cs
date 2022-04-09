using System.Configuration;
using System.Text;
using Newtonsoft.Json;
using RabbitMQ.Client;
using Rbnb.UtilisateuService.Dtos;
using Rbnb.UtilisateuService.Entities;
using Rbnb.UtilisateuService.Models;


namespace Rbnb.UtilisateuService.Services;

public class UtilisateurService : IUtilisateurService
{
    public KeycloakClient _keycloakClient { get; private set; }
    public UserServiceDbContext _dbContext { get; private set; }

    private const string Exchange_Remove = "rbnb_utilisateurs_remove";
    private const string Exchange_Update = "rbnb_utilisateurs_update";

    private const string Exchange_Mailing = "rbnb_mailing";
    private const string Mail_Info_RoutingKey = "mailing.info";


    public IModel _channel;
    public UtilisateurService(IConfiguration config, UserServiceDbContext dbContext)
    {

        _keycloakClient = new KeycloakClient(config);

        _dbContext = dbContext;

        ConnectionFactory connectionFactory = new ConnectionFactory
            {
                HostName = config["rabbitmq:host"],
                Port = Int32.Parse(config["rabbitmq:port"]),
                UserName = config["rabbitmq:username"],
                Password = config["rabbitmq:password"],
            };


            var connection = connectionFactory.CreateConnection();

            _channel = connection.CreateModel();

            _channel.ExchangeDeclare(Exchange_Remove, ExchangeType.Fanout);


    }

    public void PublishMail(MailMessage mail){
        var message = JsonConvert.SerializeObject(mail);
        var body = Encoding.UTF8.GetBytes(message);
            _channel.BasicPublish(exchange: Exchange_Mailing,
                                    routingKey: Mail_Info_RoutingKey,
                                    basicProperties: null,
                                    body: body);
    }
    public async Task<Utilisateur> InscrireUtilisateurAsync(CreationUtilisateurDto dto)
    {
        if (!dto.CheckPassword())
        {
            throw new InformationsIncorrectesException();
        }

        var utilisateurKeycloakResponse = await _keycloakClient.CreateUser(dto);

        if (utilisateurKeycloakResponse.IsSuccessStatusCode)
        {
            Utilisateur utilisateur = Mapper.ConvertCreationUtilisateurDtoToUtilisateur(dto);
            utilisateur.Id = utilisateurKeycloakResponse.UserId;
            _dbContext.Utilisateurs.Add(utilisateur);
            _dbContext.SaveChanges();
            PublishMail(new MailMessage(){
                toEmail = dto.Mail,
                subject = "Binvenue",
                content = "Bienvenu chez Rbnb " +dto.Prenom
            });
            return utilisateur;
        }
        throw new UtilisateurNonCreeException();

    }

    public Utilisateur GetUtilisateurById(string id)
    {
        var user = _dbContext.Utilisateurs.Where(u => u.Id == id).FirstOrDefault();
        if (user == null)
        {
            throw new UtilisateurNonTrouveException();
        }
        return user;
    }

    public IEnumerable<Utilisateur> GetAllUtilisateur()
    {
        return _dbContext.Utilisateurs.AsEnumerable();
    }

    public String testPublish(){
        return "published";
    }

    public void UpdateUtilisateur(string id,UtilisateurUpdateDto utilisateur)
    {
        Utilisateur user = _dbContext.Utilisateurs.Find(id);
        if(user != null){
            user.Bio = utilisateur.Bio;
            user.PhotoUrl = utilisateur.PhotoUrl;
            _dbContext.Utilisateurs.Update(user);
            if(user.Mail != utilisateur.Mail){
                System.Console.WriteLine("update mail in keycloak...");
                var res = _keycloakClient.UpdateMailAsync(id, utilisateur.Mail);
                user.Mail = utilisateur.Mail;
            }
            _dbContext.SaveChanges();

             PublishMail(new MailMessage(){
                toEmail = user.Mail,
                subject = "Votre compte est bien à jour !",
                content = "Votre compte est bien à jour ! \n Cordialement"
            });
        }
        else
            throw new UtilisateurNonTrouveException();
    }

    public void RemoveUtilisateur(string id)
    {
        Utilisateur user = _dbContext.Utilisateurs.Find(id);
        if(user != null){
            _dbContext.Utilisateurs.Remove(user);
            var deletedKeycloak = _keycloakClient.DeleteUserAsync(id).Result;
            if(deletedKeycloak){
                _dbContext.SaveChanges();
                 var message = JsonConvert.SerializeObject( user);
                var body = Encoding.UTF8.GetBytes(user.Id);

                _channel.BasicPublish(exchange: Exchange_Remove,
                                        routingKey: "",
                                    basicProperties: null,
                                    body: body);
            }
        }
        else
            throw new UtilisateurNonTrouveException();
    }
}