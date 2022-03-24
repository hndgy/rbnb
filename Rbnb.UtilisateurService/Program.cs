global using System.ComponentModel.DataAnnotations;
global using System.ComponentModel.DataAnnotations.Schema;
global using Microsoft.EntityFrameworkCore;
using System.Configuration;
using System.Security.Claims;
using Consul;
using Microsoft.AspNetCore.Authentication;
using Microsoft.AspNetCore.Authentication.JwtBearer;
using Microsoft.IdentityModel.Logging;
using Microsoft.IdentityModel.Tokens;
using Newtonsoft.Json;
using Rbnb.UtilisateuService.Models;
using Rbnb.UtilisateuService.Services;

var builder = WebApplication.CreateBuilder(args);
System.Console.WriteLine("Setting up the dbcontext with : " + builder.Configuration["ConnectionStrings:DefaultConnection"]);

// Add services to the container.
builder.Services.AddDbContext<UserServiceDbContext>(
    options =>
    {
        var conString = builder.Configuration["ConnectionStrings:DefaultConnection"];
        System.Console.WriteLine("Setting up the dbcontext with : " + conString);

        options.UseMySql(conString, ServerVersion.AutoDetect(conString));

    }
);

builder.Services.AddScoped<IUtilisateurService, UtilisateurService>();
builder.Services.AddTransient<IClaimsTransformation, ClaimsTransformer>();
builder.Services.AddControllers();
// Learn more about configuring Swagger/OpenAPI at https://aka.ms/aspnetcore/swashbuckle
builder.Services.AddEndpointsApiExplorer();
builder.Services.AddSwaggerGen();


builder.Services.AddSingleton<IConsulClient, ConsulClient>(
    p => new ConsulClient(consulConfig =>
    {
        consulConfig.Address = new Uri(builder.Configuration["consul:address"]);
    })
);


IdentityModelEventSource.ShowPII = true;

builder.Services.AddAuthentication(options =>
          {
              options.DefaultAuthenticateScheme = JwtBearerDefaults.AuthenticationScheme;
              options.DefaultChallengeScheme = JwtBearerDefaults.AuthenticationScheme;

          }).AddJwtBearer(o =>
          {
              o.Authority = builder.Configuration["keycloak:host"] + "/auth/realms/" + builder.Configuration["keycloak:realm"];
              o.Audience = builder.Configuration["keycloak:client"];
              o.RequireHttpsMetadata = false;
              o.TokenValidationParameters = new TokenValidationParameters
              {
                  RoleClaimType = ClaimTypes.Role
              };

              o.Events = new JwtBearerEvents
              {
                  OnAuthenticationFailed = c =>
                  {
                      c.NoResult();
                      c.Response.StatusCode = 500;
                      c.Response.ContentType = "text/plain";


                      return c.Response.WriteAsync("An error occured processing your authentication. " + c.Exception.Message);
                  }
              };
          });

builder.Services.AddCors();


var app = builder.Build();

var consulClient = app.Services.GetRequiredService<IConsulClient>();
var logger = app.Services.GetRequiredService<ILoggerFactory>().CreateLogger("AppExtensions");
var lifetime = app.Lifetime;

//var uri = new Uri(address);
var registration = new AgentServiceRegistration()
{
    ID = builder.Configuration["consul:name"], //{uri.Port}"
    Name = builder.Configuration["consul:name"], // servie name
    Address = builder.Configuration["consul:host"], //$"{uri.Host}",
    Port = Int32.Parse(builder.Configuration["consul:port"])  // uri.Port

};

logger.LogInformation("Registering with Consul");
consulClient.Agent.ServiceDeregister(registration.ID).ConfigureAwait(true);
consulClient.Agent.ServiceRegister(registration).ConfigureAwait(true);

lifetime.ApplicationStopping.Register(() =>
{
    logger.LogInformation("Unregistering from Consul");
});

// Configure the HTTP request pipeline.
if (app.Environment.IsDevelopment())
{
    app.UseSwagger();
    app.UseSwaggerUI();
}


app.UseHttpsRedirection();
app.UseAuthentication();
app.UseAuthorization();

app.MapControllers();

app.Run();

public class ClaimsTransformer : IClaimsTransformation
{
    public Task<ClaimsPrincipal> TransformAsync(ClaimsPrincipal principal)
    {
        var claimsIdentity = (ClaimsIdentity)principal.Identity;
        // flatten realm_access because Microsoft identity model doesn't support nested claims
        // by map it to Microsoft identity model, because automatic JWT bearer token mapping already processed here
        if (claimsIdentity.IsAuthenticated && claimsIdentity.HasClaim(claim => claim.Type == "realm_access"))
        {
            var realmAccessClaim = claimsIdentity.FindFirst(claim => claim.Type == "realm_access");
            var realmAccessAsDict = JsonConvert.DeserializeObject<Dictionary<string, string[]>>(realmAccessClaim.Value);
            if (realmAccessAsDict["roles"] != null)
            {
                foreach (var role in realmAccessAsDict["roles"])
                {
                    claimsIdentity.AddClaim(new Claim(claimsIdentity.RoleClaimType, role));
                }
            }
        }
        return Task.FromResult(principal);
    }
}