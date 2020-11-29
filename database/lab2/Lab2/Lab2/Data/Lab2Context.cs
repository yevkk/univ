using Microsoft.EntityFrameworkCore;
using Lab2.Models;

namespace Lab2.Data
{
    public class Lab2Context : DbContext
    {
        public Lab2Context (DbContextOptions<Lab2Context> options) :
            base(options)
        { 
        }

        public DbSet<Mounting> Mounting { get; set; }
        public DbSet<Camera> Camera { get; set; }
        public DbSet<Lens> Lens { get; set; }
        public DbSet<Account> Account { get; set; }
        public DbSet<Post> Post { get; set; }
    }
}
