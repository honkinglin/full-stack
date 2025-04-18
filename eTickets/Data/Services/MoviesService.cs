using eTickets.Data.Base;
using eTickets.Data.ViewModels;
using eTickets.Models;
using Microsoft.EntityFrameworkCore;
using System.IO;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Hosting;

namespace eTickets.Data.Services
{
    // MoviesService inherits from EntityBaseRepository and implements IMoviesService
    public class MoviesService : EntityBaseRepository<Movie>, IMoviesService
    {
        private readonly AppDbContext _context;
        private readonly IWebHostEnvironment _webHostEnvironment; // Used for file deletion operations

        // Constructor with dependency injection for database context and hosting environment
        public MoviesService(AppDbContext context, IWebHostEnvironment webHostEnvironment) : base(context)
        {
            _context = context;
            _webHostEnvironment = webHostEnvironment;
        }

        // Add a new movie and its associated actors to the database
        public async Task AddNewMovieAsync(NewMovieVM data, string imageUrl)
        {
            // Create a new Movie entity from the view model data
            var newMovie = new Movie()
            {
                Name = data.Name,
                Description = data.Description,
                Price = data.Price,
                ImageURL = imageUrl,
                CinemaId = data.CinemaId,
                StartDate = data.StartDate,
                EndDate = data.EndDate,
                MovieCategory = data.MovieCategory,
                ProducerId = data.ProducerId
            };
            // Add movie to database and save changes
            await _context.Movies.AddAsync(newMovie);
            await _context.SaveChangesAsync();

            // Add actor-movie relationships for each selected actor
            foreach (var actorId in data.ActorIds)
            {
                var newActorMovie = new Actor_Movie()
                {
                    MovieId = newMovie.Id,
                    ActorId = actorId
                };
                await _context.Actors_Movies.AddAsync(newActorMovie);
            }
            // Save actor-movie relationships
            await _context.SaveChangesAsync();
        }

        // Retrieve a movie by ID with related data (Cinema, Producer, Actors)
        public async Task<Movie> GetMovieByIdAsync(int id)
        {
            // Use eager loading to include related entities
            var movieDetails = await _context.Movies
                .Include(c => c.Cinema) // Include related Cinema
                .Include(p => p.Producer) // Include related Producer
                .Include(am => am.Actors_Movies).ThenInclude(a => a.Actor) // Include Actors through Actor_Movie
                .FirstOrDefaultAsync(n => n.Id == id);

            return movieDetails;
        }

        // Retrieve dropdown values for creating or editing a movie
        public async Task<NewMovieDropdownsVM> GetNewMovieDropdownsValues()
        {
            // Populate view model with sorted lists of Actors, Cinemas, and Producers
            var response = new NewMovieDropdownsVM()
            {
                Actors = await _context.Actors.OrderBy(n => n.FullName).ToListAsync(),
                Cinemas = await _context.Cinemas.OrderBy(n => n.Name).ToListAsync(),
                Producers = await _context.Producers.OrderBy(n => n.FullName).ToListAsync()
            };
            return response;
        }

        // Update an existing movie and its associated actors
        public async Task UpdateMovieAsync(NewMovieVM data, string imageUrl)
        {
            // Retrieve the existing movie by ID
            var dbMovie = await _context.Movies.FirstOrDefaultAsync(n => n.Id == data.Id);
            if (dbMovie != null)
            {
                // Update movie properties with new data
                dbMovie.Name = data.Name;
                dbMovie.Description = data.Description;
                dbMovie.Price = data.Price;
                dbMovie.ImageURL = imageUrl;
                dbMovie.CinemaId = data.CinemaId;
                dbMovie.StartDate = data.StartDate;
                dbMovie.EndDate = data.EndDate;
                dbMovie.MovieCategory = data.MovieCategory;
                dbMovie.ProducerId = data.ProducerId;
                await _context.SaveChangesAsync();
            }

            // Remove existing actor-movie relationships
            var existingActorsDb = _context.Actors_Movies.Where(n => n.MovieId == data.Id).ToList();
            _context.Actors_Movies.RemoveRange(existingActorsDb);
            await _context.SaveChangesAsync();

            // Add new actor-movie relationships based on updated ActorIds
            foreach (var actorId in data.ActorIds)
            {
                var newActorMovie = new Actor_Movie()
                {
                    MovieId = data.Id,
                    ActorId = actorId
                };
                await _context.Actors_Movies.AddAsync(newActorMovie);
            }
            await _context.SaveChangesAsync();
        }

        // Delete a movie, its associated actor relationships, and image file
        public async Task DeleteMovieAsync(int id)
        {
            // Retrieve the movie with its actor-movie relationships
            var movie = await _context.Movies
                .Include(am => am.Actors_Movies)
                .FirstOrDefaultAsync(n => n.Id == id);

            if (movie == null) return;

            // Delete the associated image file from the server
            if (!string.IsNullOrEmpty(movie.ImageURL))
            {
                string imagePath = Path.Combine(_webHostEnvironment.WebRootPath, movie.ImageURL.TrimStart('/'));
                if (File.Exists(imagePath))
                {
                    File.Delete(imagePath);
                }
            }

            // Delete all associated actor-movie relationships
            _context.Actors_Movies.RemoveRange(movie.Actors_Movies);

            // Delete the movie record
            _context.Movies.Remove(movie);
            await _context.SaveChangesAsync();
        }
    }
}