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
    public KeycloakClient(IConfiguration config)
    {
        _httpClient = new HttpClient();
        _host = config.GetValue<string>("keycloak:host");
        _reaml = config.GetValue<string>("keycloak:realm");
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


    private async Task<bool> ResetPasswordAsync(string idUser, string password)
    {
        _httpClient.DefaultRequestHeaders.Clear();
        _httpClient.DefaultRequestHeaders.Accept.Add(new MediaTypeWithQualityHeaderValue("application/json"));

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

        return response.Result.IsSuccessStatusCode;
    }

    private async Task<CreationUtilisateurKeycloakResponse> CreateUserAsync(CreationUtilisateurDto dto, string adminToken)
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
            username = dto.Username
        };

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
        var adminToken = await GetTokenAsync("admin-cli", "admin", "admin");

        Console.WriteLine("Token admin =" + adminToken);

        var userCreationResponse = await CreateUserAsync(dto, adminToken);

        if (userCreationResponse.IsSuccessStatusCode)
        {
            await ResetPasswordAsync(userCreationResponse.UserId, dto.Password);
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