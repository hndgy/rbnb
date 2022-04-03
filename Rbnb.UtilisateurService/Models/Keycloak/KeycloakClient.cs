using System.Net.Http.Headers;
using System.Text;
using Newtonsoft.Json;
using Rbnb.UtilisateuService.Dtos;

namespace Rbnb.UtilisateuService.Models;
public class KeycloakClient
{
    private HttpClient _httpClient;
    private String _reaml;
    private String _host;
    private IConfiguration _config;
    public KeycloakClient(IConfiguration config)
    {
        _httpClient = new HttpClient();
        _host = config.GetValue<string>("keycloak:host");
        _reaml = config.GetValue<string>("keycloak:realm");
        _config = config;

        System.Console.WriteLine("KeycloakClient init with host=" + _host + " && realm= " + _reaml);
    }

    public async Task<String> GetTokenAsync(String clientId, String username, String password)
    {
        _httpClient.DefaultRequestHeaders.Clear();
        _httpClient.DefaultRequestHeaders.Accept.Add(new MediaTypeWithQualityHeaderValue("application/x-www-form-urlencoded"));
        var content = new FormUrlEncodedContent(new[]{
                new KeyValuePair<string, string>("client_id", "admin-cli"),
                new KeyValuePair<string, string>("username",username),
                new KeyValuePair<string, string>("password",password),
                new KeyValuePair<string, string>("grant_type","password")
        });

        var response = _httpClient.PostAsync(
            _host + "/auth/realms/master/protocol/openid-connect/token",
            content
        );

        var finalData = await response.Result.Content.ReadAsStringAsync();
        var _dataResponse = JsonConvert.DeserializeObject<KeycloakTokenResponseModel>(finalData);
        return _dataResponse.Access_token;
    }


    private bool ResetPassword(string idUser, string password, string adminToken)
    {
        _httpClient.DefaultRequestHeaders.Clear();
        _httpClient.DefaultRequestHeaders.Accept.Add(new MediaTypeWithQualityHeaderValue("application/json"));
        _httpClient.DefaultRequestHeaders.Authorization = new AuthenticationHeaderValue("Bearer", adminToken);


        var obj = new
        {
            type = "password",
            value = password,
            temporary = false
        };

        var content = new StringContent(
            JsonConvert.SerializeObject(obj),
            Encoding.UTF8,
            "application/json"
        );


        Console.WriteLine(_host + "/auth/admin/realms/" + _reaml + "/users/" + idUser + "/reset-password");
        var response = _httpClient.PutAsync(
            _host + "/auth/admin/realms/" + _reaml + "/users/" + idUser + "/reset-password",
            content
        );
        response.Wait();

        System.Console.WriteLine(response.Result.RequestMessage);
        return response.Result.IsSuccessStatusCode;
    }

    /*
    => /auth/admin/realms/rbnb/users/fff1c771-5cc5-4db0-ba50-97271d2d242a/role-mappings/realm
    [{"id":"2e4251db-f8f9-45d4-a029-54bd80eeec2f","name":"USER","composite":false,"clientRole":false,"containerId":"rbnb"}]
    */
    private bool AddRole(string idUser, string role, string adminToken)
    {
        _httpClient.DefaultRequestHeaders.Clear();
        _httpClient.DefaultRequestHeaders.Accept.Add(new MediaTypeWithQualityHeaderValue("application/json"));
        _httpClient.DefaultRequestHeaders.Authorization = new AuthenticationHeaderValue("Bearer", adminToken);


        var obj = new
        {
            id = idUser,
            name = role,
            composite = false,
            clientRole = false,
            containerId = _reaml
        };
        var tab = new object[] { obj };


        var content = new StringContent(
            JsonConvert.SerializeObject(tab),
            Encoding.UTF8,
            "application/json"
        );


        Console.WriteLine(_host + "/auth/admin/realms/" + _reaml + "/users/" + idUser + "/role-mappings/realm");
        var response = _httpClient.PostAsync(
            _host + "/auth/admin/realms/" + _reaml + "/users/" + idUser + "/role-mappings/realm",
            content
        );
        response.Wait();
        System.Console.WriteLine(response.Result.RequestMessage);

        return response.Result.IsSuccessStatusCode;
    }
    private CreationUtilisateurKeycloakResponse CreateUser(CreationUtilisateurDto dto, string adminToken)
    {
        _httpClient.DefaultRequestHeaders.Clear();
        _httpClient.DefaultRequestHeaders.Accept.Add(new MediaTypeWithQualityHeaderValue("application/json"));
        _httpClient.DefaultRequestHeaders.Authorization = new AuthenticationHeaderValue("Bearer", adminToken);

        var obj = new
        {
            firstName = dto.Prenom,
            lastName = dto.Nom,
            email = dto.Mail,
            enabled = true,
            username = dto.Username,
            groups = new string[]{"/"+dto.Role }
        };

        System.Console.WriteLine(obj);
        var content = new StringContent(
            JsonConvert.SerializeObject(obj),
            Encoding.UTF8,
            "application/json"
        );
        Console.WriteLine(_host + "/auth/admin/realms/" + _reaml + "/users");

        var response = _httpClient.PostAsync(
            _host + "/auth/admin/realms/" + _reaml + "/users",
            content
        );
        response.Wait();
        Console.WriteLine(response.Result.Content.ReadAsStringAsync());
        var creationUtilisateurKeycloakResponse = new CreationUtilisateurKeycloakResponse();
        var locationHeader = response.Result.Headers.Location;

        if (response.Result.IsSuccessStatusCode && locationHeader != null)
        {
            var locationSplit = locationHeader.AbsolutePath.Split("/");
            var userId = locationSplit[locationSplit.Length - 1];
            creationUtilisateurKeycloakResponse.UserId = userId;
        }

        creationUtilisateurKeycloakResponse.IsSuccessStatusCode = response.Result.IsSuccessStatusCode && locationHeader != null;
        return creationUtilisateurKeycloakResponse;

    }
    public async Task<CreationUtilisateurKeycloakResponse> CreateUser(CreationUtilisateurDto dto)
    {
        var adminToken = await GetTokenAsync("admin-cli", _config.GetValue<string>("keycloak:username"), _config.GetValue<string>("keycloak:password"));

        Console.WriteLine("Token admin =" + adminToken);

        var userCreationResponse = CreateUser(dto, adminToken);

        if (userCreationResponse.IsSuccessStatusCode)
        {
            var passwordIsSet = ResetPassword(userCreationResponse.UserId, dto.Password, adminToken);
            var passwordRole = AddRole(userCreationResponse.UserId, dto.Role, adminToken);

        }
        return userCreationResponse;


    }

}

public class KeycloakTokenResponseModel
{
    public string Access_token { get; set; }
}


public class CreationUtilisateurKeycloakResponse
{
    public CreationUtilisateurKeycloakResponse()
    {
    }

    public string UserId { get; set; }
    public bool IsSuccessStatusCode { get; set; }
}