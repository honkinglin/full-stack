using eTickets.Data.Static;
using eTickets.Models;
using Microsoft.AspNetCore.Builder;
using Microsoft.AspNetCore.Identity;
using Microsoft.Extensions.DependencyInjection;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.Extensions.Logging;
namespace eTickets.Data
{
    public class AppDbInitializer
    {
        public static void Seed(IApplicationBuilder applicationBuilder)
        {
            using (var serviceScope = applicationBuilder.ApplicationServices.CreateScope())
            {
                var context = serviceScope.ServiceProvider.GetService<AppDbContext>();

                context.Database.EnsureCreated();

                //Cinema
                if (!context.Cinemas.Any())
                {
                    context.Cinemas.AddRange(new List<Cinema>()
                    {
                        new Cinema()
                        {
                            Name = "Cinema 1",
                            Logo = "http://localhost:5090/images/cinemas/cinema-1.jpeg",
                            Description = "This is the description of the first cinema"
                        },
                        new Cinema()
                        {
                            Name = "Cinema 2",
                            Logo = "http://localhost:5090/images/cinemas/cinema-2.jpeg",
                            Description = "This is the description of the first cinema"
                        },
                        new Cinema()
                        {
                            Name = "Cinema 3",
                            Logo = "http://localhost:5090/images/cinemas/cinema-3.jpeg",
                            Description = "This is the description of the first cinema"
                        },
                        new Cinema()
                        {
                            Name = "Cinema 4",
                            Logo = "http://localhost:5090/images/cinemas/cinema-4.jpeg",
                            Description = "This is the description of the first cinema"
                        },
                        new Cinema()
                        {
                            Name = "Cinema 5",
                            Logo = "http://localhost:5090/images/cinemas/cinema-5.jpeg",
                            Description = "This is the description of the first cinema"
                        },
                    });
                    context.SaveChanges();
                }
                //Actors
                if (!context.Actors.Any())
                {
                    context.Actors.AddRange(new List<Actor>()
                    {
                        new Actor()
                        {
                            FullName = "Actor 1",
                            Bio = "This is the Bio of the first actor",
                            ProfilePictureURL = "http://localhost:5090/images/actors/actor-1.jpg"

                        },
                        new Actor()
                        {
                            FullName = "Actor 2",
                            Bio = "This is the Bio of the second actor",
                            ProfilePictureURL = "http://localhost:5090/images/actors/actor-2.jpg"
                        },
                        new Actor()
                        {
                            FullName = "Actor 3",
                            Bio = "This is the Bio of the second actor",
                            ProfilePictureURL = "http://localhost:5090/images/actors/actor-3.jpg"
                        },
                        new Actor()
                        {
                            FullName = "Actor 4",
                            Bio = "This is the Bio of the second actor",
                            ProfilePictureURL = "http://localhost:5090/images/actors/actor-4.jpg"
                        },
                        new Actor()
                        {
                            FullName = "Actor 5",
                            Bio = "This is the Bio of the second actor",
                            ProfilePictureURL = "http://localhost:5090/images/actors/actor-5.jpg"
                        }
                    });
                    context.SaveChanges();
                }
                //Producers
                if (!context.Producers.Any())
                {
                    context.Producers.AddRange(new List<Producer>()
                    {
                        new Producer()
                        {
                            FullName = "Producer 1",
                            Bio = "This is the Bio of the first actor",
                            ProfilePictureURL = "http://localhost:5090/images/producers/producer-1.jpeg"

                        },
                        new Producer()
                        {
                            FullName = "Producer 2",
                            Bio = "This is the Bio of the second actor",
                            ProfilePictureURL = "http://localhost:5090/images/producers/producer-2.jpeg"
                        },
                        new Producer()
                        {
                            FullName = "Producer 3",
                            Bio = "This is the Bio of the second actor",
                            ProfilePictureURL = "http://localhost:5090/images/producers/producer-3.jpeg"
                        },
                        new Producer()
                        {
                            FullName = "Producer 4",
                            Bio = "This is the Bio of the second actor",
                            ProfilePictureURL = "http://localhost:5090/images/producers/producer-4.jpeg"
                        },
                        new Producer()
                        {
                            FullName = "Producer 5",
                            Bio = "This is the Bio of the second actor",
                            ProfilePictureURL = "http://localhost:5090/images/producers/producer-5.jpeg"
                        }
                    });
                    context.SaveChanges();
                }
                //Movies
                if (!context.Movies.Any())
                {
                    context.Movies.AddRange(new List<Movie>()
                    {
                        new Movie()
                        {
                            Name = "Companion",
                            Description = "This is the Companion movie description",
                            Price = 39.50,
                            ImageURL = "http://localhost:5090/images/movies/movie-3.webp",
                            StartDate = DateTime.Now.AddDays(-10),
                            EndDate = DateTime.Now.AddDays(10),
                            CinemaId = 3,
                            ProducerId = 3,
                            MovieCategory = MovieCategory.Documentary
                        },
                        new Movie()
                        {
                            Name = "Anora",
                            Description = "This is the Anora description",
                            Price = 29.50,
                            ImageURL = "http://localhost:5090/images/movies/movie-1.webp",
                            StartDate = DateTime.Now,
                            EndDate = DateTime.Now.AddDays(10),
                            CinemaId = 1,
                            ProducerId = 1,
                            MovieCategory = MovieCategory.Action
                        },
                        new Movie()
                        {
                            Name = "Fight or Flight",
                            Description = "This is the Fight or Flight movie description",
                            Price = 39.50,
                            ImageURL = "http://localhost:5090/images/movies/movie-4.webp",
                            StartDate = DateTime.Now,
                            EndDate = DateTime.Now.AddDays(7),
                            CinemaId = 4,
                            ProducerId = 4,
                            MovieCategory = MovieCategory.Horror
                        },
                        new Movie()
                        {
                            Name = "The Substance",
                            Description = "This is the Substance movie description",
                            Price = 39.50,
                            ImageURL = "http://localhost:5090/images/movies/movie-6.webp",
                            StartDate = DateTime.Now.AddDays(-10),
                            EndDate = DateTime.Now.AddDays(5),
                            CinemaId = 1,
                            ProducerId = 2,
                            MovieCategory = MovieCategory.Documentary
                        },
                        new Movie()
                        {
                            Name = "Conclave",
                            Description = "This is the Conclave movie description",
                            Price = 39.50,
                            ImageURL = "http://localhost:5090/images/movies/movie-7.webp",
                            StartDate = DateTime.Now.AddDays(-10),
                            EndDate = DateTime.Now.AddDays(-2),
                            CinemaId = 1,
                            ProducerId = 3,
                            MovieCategory = MovieCategory.Cartoon
                        },
                        new Movie()
                        {
                            Name = "A Complete Unknown",
                            Description = "This is the A Complete Unknown movie description",
                            Price = 39.50,
                            ImageURL = "http://localhost:5090/images/movies/movie-8.webp",
                            StartDate = DateTime.Now.AddDays(3),
                            EndDate = DateTime.Now.AddDays(20),
                            CinemaId = 1,
                            ProducerId = 5,
                            MovieCategory = MovieCategory.Drama
                        }
                    });
                    context.SaveChanges();
                }
                //Actors & Movies
                if (!context.Actors_Movies.Any())
                {
                    context.Actors_Movies.AddRange(new List<Actor_Movie>()
                    {
                        new Actor_Movie()
                        {
                            ActorId = 1,
                            MovieId = 1
                        },
                        new Actor_Movie()
                        {
                            ActorId = 3,
                            MovieId = 1
                        },

                         new Actor_Movie()
                        {
                            ActorId = 1,
                            MovieId = 2
                        },
                         new Actor_Movie()
                        {
                            ActorId = 4,
                            MovieId = 2
                        },

                        new Actor_Movie()
                        {
                            ActorId = 1,
                            MovieId = 3
                        },
                        new Actor_Movie()
                        {
                            ActorId = 2,
                            MovieId = 3
                        },
                        new Actor_Movie()
                        {
                            ActorId = 5,
                            MovieId = 3
                        },


                        new Actor_Movie()
                        {
                            ActorId = 2,
                            MovieId = 4
                        },
                        new Actor_Movie()
                        {
                            ActorId = 3,
                            MovieId = 4
                        },
                        new Actor_Movie()
                        {
                            ActorId = 4,
                            MovieId = 4
                        },


                        new Actor_Movie()
                        {
                            ActorId = 2,
                            MovieId = 5
                        },
                        new Actor_Movie()
                        {
                            ActorId = 3,
                            MovieId = 5
                        },
                        new Actor_Movie()
                        {
                            ActorId = 4,
                            MovieId = 5
                        },
                        new Actor_Movie()
                        {
                            ActorId = 5,
                            MovieId = 5
                        },


                        new Actor_Movie()
                        {
                            ActorId = 3,
                            MovieId = 6
                        },
                        new Actor_Movie()
                        {
                            ActorId = 4,
                            MovieId = 6
                        },
                        new Actor_Movie()
                        {
                            ActorId = 5,
                            MovieId = 6
                        },
                    });
                    context.SaveChanges();
                }
            }

        }
// This method seeds the database with initial roles and users for the application.
        public static async Task SeedUsersAndRolesAsync(IApplicationBuilder applicationBuilder)
        {
            // Create a scoped service provider to resolve dependencies.
            using (var serviceScope = applicationBuilder.ApplicationServices.CreateScope())
            {
                // Retrieve the logger service for logging information and errors.
                var logger = serviceScope.ServiceProvider.GetService<ILogger<AppDbInitializer>>();

                // Retrieve the RoleManager service to manage roles in the application.
                var roleManager = serviceScope.ServiceProvider.GetRequiredService<RoleManager<IdentityRole>>();

                // Check if the Admin role exists; if not, create it.
                if (!await roleManager.RoleExistsAsync(UserRoles.Admin))
                    await roleManager.CreateAsync(new IdentityRole(UserRoles.Admin));

                // Check if the User role exists; if not, create it.
                if (!await roleManager.RoleExistsAsync(UserRoles.User))
                    await roleManager.CreateAsync(new IdentityRole(UserRoles.User));

                // Retrieve the UserManager service to manage users in the application.
                var userManager = serviceScope.ServiceProvider.GetRequiredService<UserManager<ApplicationUser>>();

                // Define the email for the admin user.
                string adminUserEmail = "admin@test.com";

                // Check if the admin user already exists by email.
                var adminUser = await userManager.FindByEmailAsync(adminUserEmail);
                if (adminUser == null)
                {
                    // Create a new admin user if it does not exist.
                    var newAdminUser = new ApplicationUser()
                    {
                        FullName = "Admin User", // Admin's full name.
                        UserName = "admin-user", // Admin's username.
                        Email = adminUserEmail, // Admin's email.
                        EmailConfirmed = true  // Mark the email as confirmed.
                    };

                    // Create the admin user with a predefined password.
                    await userManager.CreateAsync(newAdminUser, "Test123?");
                    // Assign the Admin role to the newly created user.
                    await userManager.AddToRoleAsync(newAdminUser, UserRoles.Admin);
                }

                // Define the email for a regular application user.
                string appUserEmail = "user@test.com";

                // Check if the regular user already exists by email.
                var appUser = await userManager.FindByEmailAsync(appUserEmail);
                if (appUser == null)
                {
                    // Create a new regular user if it does not exist.
                    var newAppUser = new ApplicationUser()
                    {
                        FullName = "Application User", // User's full name.
                        UserName = "app-user",         // User's username.
                        Email = appUserEmail,          // User's email.
                        EmailConfirmed = true          // Mark the email as confirmed.
                    };

                    // Create the user with a predefined password.
                    await userManager.CreateAsync(newAppUser, "Test123?");
                    // Assign the User role to the newly created user.
                    await userManager.AddToRoleAsync(newAppUser, UserRoles.User);
                }

                // Define the email for a second regular application user.
                string appUserEmail2 = "user2@test.com";

                // Check if the second regular user already exists by email.
                var appUser2 = await userManager.FindByEmailAsync(appUserEmail2);
                if (appUser2 == null)
                {
                    // Log the creation of the second user.
                    logger?.LogInformation("Creating app user 2: {Email}", appUserEmail2);

                    // Create a new second regular user if it does not exist.
                    var newAppUser2 = new ApplicationUser()
                    {
                        FullName = "App User2", // User's full name.
                        UserName = "app-user2", // User's username.
                        Email = appUserEmail2,  // User's email.
                        EmailConfirmed = true   // Mark the email as confirmed.
                    };

                    // Attempt to create the user with a predefined password.
                    var result = await userManager.CreateAsync(newAppUser2, "Test123!");
                    if (result.Succeeded)
                    {
                        // Assign the User role to the newly created user.
                        await userManager.AddToRoleAsync(newAppUser2, UserRoles.User);
                        // Log the successful creation of the user.
                        logger?.LogInformation("App user 2 created and assigned to User role");
                    }
                    else
                    {
                        // Log any errors that occurred during user creation.
                        logger?.LogError("Failed to create app user 2: {Errors}", string.Join(", ", result.Errors.Select(e => e.Description)));
                    }
                }
            }
        }
    }
}
