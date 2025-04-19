using eTickets.Models;
using Microsoft.AspNetCore.Identity.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore;

namespace eTickets.Data
{
    // The AppDbContext class represents the database context for the application.
    // It inherits from IdentityDbContext to include ASP.NET Core Identity functionality, 
    // with built-in support for managing users, roles, and authentication.
    public class AppDbContext : IdentityDbContext<ApplicationUser>
    {
        // Constructor that accepts DbContextOptions to configure the context.
        public AppDbContext(DbContextOptions<AppDbContext> options) : base(options)
        {
        }

        // Configures the model and relationships between entities.
        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            // Configures a composite primary key for the Actor_Movie entity.
            modelBuilder.Entity<Actor_Movie>().HasKey(am => new
            {
                am.ActorId, // Part of the composite key.
                am.MovieId  // Part of the composite key.
            });

            // Configures the relationship between Actor_Movie and Movie.
            // A Movie can have many Actor_Movie relationships.
            modelBuilder.Entity<Actor_Movie>()
                .HasOne(m => m.Movie)
                .WithMany(am => am.Actors_Movies)
                .HasForeignKey(m => m.MovieId);

            // Configures the relationship between Actor_Movie and Actor.
            // An Actor can have many Actor_Movie relationships.
            modelBuilder.Entity<Actor_Movie>()
                .HasOne(m => m.Actor)
                .WithMany(am => am.Actors_Movies)
                .HasForeignKey(m => m.ActorId);

            // Calls the base class's OnModelCreating to ensure Identity-related configurations are applied.
            base.OnModelCreating(modelBuilder);
        }

        // DbSet properties represent tables in the database.

        // Table for storing Actor entities.
        public DbSet<Actor> Actors { get; set; }

        // Table for storing Movie entities.
        public DbSet<Movie> Movies { get; set; }

        // Table for storing the many-to-many relationship between Actors and Movies.
        public DbSet<Actor_Movie> Actors_Movies { get; set; }

        // Table for storing Cinema entities.
        public DbSet<Cinema> Cinemas { get; set; }

        // Table for storing Producer entities.
        public DbSet<Producer> Producers { get; set; }

        // Orders-related tables:

        // Table for storing Order entities.
        public DbSet<Order> Orders { get; set; }

        // Table for storing OrderItem entities, which represent items in an order.
        public DbSet<OrderItem> OrderItems { get; set; }

        // Table for storing ShoppingCartItem entities, which represent items in a shopping cart.
        public DbSet<ShoppingCartItem> ShoppingCartItems { get; set; }
    }
}
