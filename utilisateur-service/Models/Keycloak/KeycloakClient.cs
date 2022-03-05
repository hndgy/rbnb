using System.Net.Http.Headers;
using System.Text;
using Newtonsoft.Json;
using utilisateur_service.Dtos;

namespace utilisateur_service.Models;
public class KeycloakClient
{

    private HttpClient _httpClient;
    private String _reaml;
    private String _host;
    public KeycloakClient(String host, String realm)
    {
        _httpClient = new HttpClient();
        _host = host;
        _reaml = realm;
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
            _host + "/realms/master/protocol/openid-connect/token",
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

        var response = _httpClient.PostAsync(
            _host + "/admin/realms/" + _reaml + "/users/" + idUser + "/reset-password",
            content
        );
        response.Wait();

        return response.Result.IsSuccessStatusCode;
    }

    private async Task<string> CreateUserAsync(CreationUtilisateurDto dto, string adminToken)
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
        Console.WriteLine(_host + "/admin/realms/" + _reaml + "/users");

        var response = _httpClient.PostAsync(
            _host + "/admin/realms/" + _reaml + "/users",
            content
        );
        response.Wait();
        Console.WriteLine(response.Result.IsSuccessStatusCode);
        return Guid.NewGuid().ToString();
        var userId = response.Result.Headers.Location.AbsolutePath.Split("/")[-1];

        return userId;
    }
    public async Task<string> CreateUser(CreationUtilisateurDto dto)
    {
        var adminToken = await GetTokenAsync("admin-cli", "admin", "admin");

        var idUser = await CreateUserAsync(dto, adminToken);
        await ResetPasswordAsync(idUser, dto.Password);

        return idUser;

    }

}

public class KeycloakTokenResponseModel
{
    public String Access_token { get; set; }
}