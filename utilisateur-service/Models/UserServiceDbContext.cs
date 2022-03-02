using Microsoft.EntityFrameworkCore;
using utilisateur_service.Entities;

namespace utilisateur_service.Models;
public class UserServiceDbContext : DbContext
{
    public UserServiceDbContext(DbContextOptions<UserServiceDbContext> options)
        : base(options)
    { }

    public DbSet<Utilisateur> Utilisateurs { get; set; }
}