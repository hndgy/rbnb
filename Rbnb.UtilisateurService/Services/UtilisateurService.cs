using System.Configuration;
using System.Text;
using Newtonsoft.Json;
using RabbitMQ.Client;
using Rbnb.UtilisateuService.Dtos;
using Rbnb.UtilisateuService.Entities;
using Rbnb.UtilisateuService.Models;
using Rbnb.UtilisateuService.RabbitMQ;

namespace Rbnb.UtilisateuService.Services;

public class UtilisateurService : IUtilisateurService
{
    public KeycloakClient _keycloakClient { get; private set; }
    public UserServiceDbContext _dbContext { get; private set; }

    private const string Exchange_Remove = "rbnb_utilisateurs_remove";
    private const string Exchange_Update = "rbnb_utilisateurs_update";
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
        Producer.Publish(_channel,"rbnb_utilisateurs_create","testkey", new {message = "test"});
        return "published";
    }

    public void UpdateUtilisateur(Utilisateur utilisateur)
    {
        if(_dbContext.Utilisateurs.Find(utilisateur.Id) != null){
            _dbContext.Utilisateurs.Update(utilisateur);
            _dbContext.SaveChanges();
            var message = JsonConvert.SerializeObject( utilisateur);
            var body = Encoding.UTF8.GetBytes(message);
            _channel.BasicPublish(exchange: Exchange_Update,
                                    routingKey: "",
                                    basicProperties: null,
                                    body: body);
        }
        else
            throw new UtilisateurNonTrouveException();
    }

    public void RemoveUtilisateur(string id)
    {
        Utilisateur user = _dbContext.Utilisateurs.Find(id);
        if(user != null){
            _dbContext.Utilisateurs.Remove(user);
            _dbContext.SaveChanges();
            var message = JsonConvert.SerializeObject( user);
            var body = Encoding.UTF8.GetBytes(message);

            _channel.BasicPublish(exchange: Exchange_Remove,
                                    routingKey: "",
                                    basicProperties: null,
                                    body: body);
        }
        else
            throw new UtilisateurNonTrouveException();
    }
}