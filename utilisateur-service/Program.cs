global using System.ComponentModel.DataAnnotations;
global using System.ComponentModel.DataAnnotations.Schema;
global using Microsoft.EntityFrameworkCore;
using System.Configuration;
using Consul;
using utilisateur_service.Models;

var builder = WebApplication.CreateBuilder(args);

// Add services to the container.
var connectionString = builder.Configuration.GetConnectionString("DefaultConnection");
builder.Services.AddDbContext<UserServiceDbContext>(
    options =>
    {
        options.UseMySql("server=localhost;database=Utilisateur;user=root;password=p", ServerVersion.AutoDetect("server=localhost;database=Utilisateur;user=root;password=p"));
    }
);
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

var app = builder.Build();
var consulClient = app.Services.GetRequiredService<IConsulClient>();
var logger = app.Services.GetRequiredService<ILoggerFactory>().CreateLogger("AppExtensions");
var lifetime = app.Lifetime;

//var uri = new Uri(address);
var registration = new AgentServiceRegistration()
{
    ID = builder.Configuration["consul:name"], //{uri.Port}",
                                               // servie name  
    Name = builder.Configuration["consul:name"],
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

app.MapControllers();

app.Run();
