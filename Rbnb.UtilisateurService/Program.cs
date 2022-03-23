global using System.ComponentModel.DataAnnotations;
global using System.ComponentModel.DataAnnotations.Schema;
global using Microsoft.EntityFrameworkCore;
using System.Configuration;
using Consul;
using Microsoft.AspNetCore.Authentication.JwtBearer;
using Rbnb.UtilisateuService.Models;
using Rbnb.UtilisateuService.Services;

var builder = WebApplication.CreateBuilder(args);

// Add services to the container.
builder.Services.AddDbContext<UserServiceDbContext>(
    options =>
    {
        var conString = "server=localhost;database=UtilisateurService;port=3307;user=user;password=1234";//builder.Configuration["ConnectionStrings:DefaultConnection"];
        options.UseMySql(conString, ServerVersion.AutoDetect(builder.Configuration["ConnectionStrings:DefaultConnection"]));
    }
);

builder.Services.AddScoped<IUtilisateurService, UtilisateurService>();
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




builder.Services.AddAuthentication(options =>
{
    options.DefaultAuthenticateScheme =
JwtBearerDefaults.AuthenticationScheme;
    options.DefaultChallengeScheme =
JwtBearerDefaults.AuthenticationScheme;
}).AddJwtBearer(o =>
{
    o.Authority = "http://localhost:9001/auth/realms/rbnb";//builder.Configuration["keycloak:host"];
    o.Audience = "rbnb";//builder.Configuration["Keycloak:Audience"];
    o.RequireHttpsMetadata = false;
    o.Events = new JwtBearerEvents()
    {
        OnAuthenticationFailed = c =>
        {
            c.NoResult();
            c.Response.StatusCode = 500;
            c.Response.ContentType = "text/plain";

            return c.Response.WriteAsync(c.Exception.ToString());
        }
    };
});
var app = builder.Build();

var consulClient = app.Services.GetRequiredService<IConsulClient>();
var logger = app.Services.GetRequiredService<ILoggerFactory>().CreateLogger("AppExtensions");
var lifetime = app.Lifetime;

//var uri = new Uri(address);
var registration = new AgentServiceRegistration()
{
    ID = builder.Configuration["consul:name"], //{uri.Port}"
    Name = builder.Configuration["consul:name"], // servie name 
    Address = builder.Configuration["consul:address"], //$"{uri.Host}",
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

app.UseAuthorization();
app.UseAuthentication();

app.MapControllers();

app.Run();
