using Microsoft.EntityFrameworkCore;
using utilisateur_service.Entities;

namespace utilisateur_service.Models;
public class UserServiceDbContext : DbContext
{

    public UserServiceDbContext()
    {
    }
    public UserServiceDbContext(DbContextOptions<UserServiceDbContext> options)
        : base(options)
    {
    }

    public DbSet<Utilisateur> Utilisateurs { get; set; }

    protected override void OnConfiguring(DbContextOptionsBuilder optionsBuilder)
    {
        if (!optionsBuilder.IsConfigured)
        {
            var connectionString = "Server=localhost;Port=3306;Database=UtilisateurService;User=user;Password=1234";
            optionsBuilder.UseMySql(
                connectionString,
                ServerVersion.AutoDetect(connectionString)
            );
        }
    }
}