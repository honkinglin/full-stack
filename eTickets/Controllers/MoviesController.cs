using eTickets.Data.Services;
using eTickets.Data.Static;
using eTickets.Models;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.Rendering;
using System;
using System.IO;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Hosting;

namespace eTickets.Controllers
{
    // Restrict access to Admin role for this controller
    [Authorize(Roles = UserRoles.Admin)]
    public class MoviesController : Controller
    {
        private readonly IMoviesService _service;
        private readonly IWebHostEnvironment _webHostEnvironment;

        // Constructor with dependency injection for service and hosting environment
        public MoviesController(IMoviesService service, IWebHostEnvironment webHostEnvironment)
        {
            _service = service;
            _webHostEnvironment = webHostEnvironment;
        }

        // Allow anonymous access to view all movies with pagination
        [AllowAnonymous]
        public async Task<IActionResult> Index(int page = 1, int pageSize = 6)
        {
            // Retrieve all movies with related Cinema data
            var allMovies = await _service.GetAllAsync(n => n.Cinema);
            var totalMovies = allMovies.Count();
            // Calculate total pages for pagination
            var totalPages = (int)Math.Ceiling((double)totalMovies / pageSize);

            // Paginate movies based on current page and page size
            var paginatedMovies = allMovies
                .Skip((page - 1) * pageSize)
                .Take(pageSize)
                .ToList();

            // Pass pagination data to view
            ViewBag.CurrentPage = page;
            ViewBag.TotalPages = totalPages;
            ViewBag.PageSize = pageSize;

            return View(paginatedMovies);
        }

        // Allow anonymous access to filter movies by search string with pagination
        [AllowAnonymous]
        public async Task<IActionResult> Filter(string searchString, int page = 1, int pageSize = 6)
        {
            // Retrieve all movies with related Cinema data
            var allMovies = await _service.GetAllAsync(n => n.Cinema);

            // Filter movies by name or description if search string is provided
            if (!string.IsNullOrEmpty(searchString))
            {
                allMovies = allMovies.Where(n => string.Equals(n.Name, searchString, StringComparison.CurrentCultureIgnoreCase) ||
                                                 string.Equals(n.Description, searchString, StringComparison.CurrentCultureIgnoreCase)).ToList();
            }

            var totalMovies = allMovies.Count();
            // Calculate total pages for pagination
            var totalPages = (int)Math.Ceiling((double)totalMovies / pageSize);

            // Paginate filtered movies
            var paginatedMovies = allMovies
                .Skip((page - 1) * pageSize)
                .Take(pageSize)
                .ToList();

            // Pass pagination and search data to view
            ViewBag.CurrentPage = page;
            ViewBag.TotalPages = totalPages;
            ViewBag.PageSize = pageSize;
            ViewBag.SearchString = searchString;

            // Reuse Index view for filtered results
            return View("Index", paginatedMovies);
        }

        // Allow anonymous access to view movie details
        [AllowAnonymous]
        public async Task<IActionResult> Details(int id)
        {
            var movieDetail = await _service.GetMovieByIdAsync(id);
            return View(movieDetail);
        }

        // GET: Movies/Create
        public async Task<IActionResult> Create()
        {
            // Retrieve dropdown data for cinemas, producers, and actors
            var movieDropdownsData = await _service.GetNewMovieDropdownsValues();

            // Populate ViewBag with SelectList for dropdowns
            ViewBag.Cinemas = new SelectList(movieDropdownsData.Cinemas, "Id", "Name");
            ViewBag.Producers = new SelectList(movieDropdownsData.Producers, "Id", "FullName");
            ViewBag.Actors = new SelectList(movieDropdownsData.Actors, "Id", "FullName");

            return View();
        }

        // POST: Movies/Create
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> Create(NewMovieVM movie)
        {
            // Validate uploaded image file
            if (movie.ImageFile == null || movie.ImageFile.Length == 0)
            {
                ModelState.AddModelError("ImageFile", "Movie poster is required");
            }

            // Return to view with validation errors if model state is invalid
            if (!ModelState.IsValid)
            {
                var movieDropdownsData = await _service.GetNewMovieDropdownsValues();
                ViewBag.Cinemas = new SelectList(movieDropdownsData.Cinemas, "Id", "Name");
                ViewBag.Producers = new SelectList(movieDropdownsData.Producers, "Id", "FullName");
                ViewBag.Actors = new SelectList(movieDropdownsData.Actors, "Id", "FullName");
                return View(movie);
            }

            string imageUrl = null;
            // Handle image upload if provided
            if (movie.ImageFile != null && movie.ImageFile.Length > 0)
            {
                // Set up directory for storing movie posters
                string uploadsFolder = Path.Combine(_webHostEnvironment.WebRootPath, "images/movies");
                if (!Directory.Exists(uploadsFolder))
                {
                    Directory.CreateDirectory(uploadsFolder);
                }

                // Generate unique filename to prevent conflicts
                string uniqueFileName = Guid.NewGuid().ToString() + "_" + movie.ImageFile.FileName;
                string filePath = Path.Combine(uploadsFolder, uniqueFileName);

                // Save uploaded file to server
                using (var fileStream = new FileStream(filePath, FileMode.Create))
                {
                    await movie.ImageFile.CopyToAsync(fileStream);
                }

                imageUrl = "/images/movies/" + uniqueFileName;
            }

            // Add new movie to database with image URL
            await _service.AddNewMovieAsync(movie, imageUrl);
            return RedirectToAction(nameof(Index));
        }

        // GET: Movies/Edit/1
        public async Task<IActionResult> Edit(int id)
        {
            var movieDetails = await _service.GetMovieByIdAsync(id);
            if (movieDetails == null) return View("NotFound");

            // Map movie data to view model for editing
            var response = new NewMovieVM()
            {
                Id = movieDetails.Id,
                Name = movieDetails.Name,
                Description = movieDetails.Description,
                Price = movieDetails.Price,
                StartDate = movieDetails.StartDate,
                EndDate = movieDetails.EndDate,
                MovieCategory = movieDetails.MovieCategory,
                CinemaId = movieDetails.CinemaId,
                ProducerId = movieDetails.ProducerId,
                ActorIds = movieDetails.Actors_Movies.Select(n => n.ActorId).ToList(),
            };

            // Populate dropdowns for cinemas, producers, and actors
            var movieDropdownsData = await _service.GetNewMovieDropdownsValues();
            ViewBag.Cinemas = new SelectList(movieDropdownsData.Cinemas, "Id", "Name");
            ViewBag.Producers = new SelectList(movieDropdownsData.Producers, "Id", "FullName");
            ViewBag.Actors = new SelectList(movieDropdownsData.Actors, "Id", "FullName");
            // Pass current image URL to view for display
            ViewBag.CurrentImage = movieDetails.ImageURL;

            return View(response);
        }

        // POST: Movies/Edit/1
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> Edit(int id, NewMovieVM movie)
        {
            // Verify ID consistency
            if (id != movie.Id) return View("NotFound");

            // Return to view with validation errors if model state is invalid
            if (!ModelState.IsValid)
            {
                var movieDropdownsData = await _service.GetNewMovieDropdownsValues();
                ViewBag.Cinemas = new SelectList(movieDropdownsData.Cinemas, "Id", "Name");
                ViewBag.Producers = new SelectList(movieDropdownsData.Producers, "Id", "FullName");
                ViewBag.Actors = new SelectList(movieDropdownsData.Actors, "Id", "FullName");
                return View(movie);
            }

            // Retrieve existing image URL
            string imageUrl = (await _service.GetMovieByIdAsync(id)).ImageURL;

            // Handle new image upload if provided
            if (movie.ImageFile != null && movie.ImageFile.Length > 0)
            {
                // Set up directory for storing movie posters
                string uploadsFolder = Path.Combine(_webHostEnvironment.WebRootPath, "images/movies");
                if (!Directory.Exists(uploadsFolder))
                {
                    Directory.CreateDirectory(uploadsFolder);
                }

                // Generate unique filename to prevent conflicts
                string uniqueFileName = Guid.NewGuid().ToString() + "_" + movie.ImageFile.FileName;
                string filePath = Path.Combine(uploadsFolder, uniqueFileName);

                // Save uploaded file to server
                using (var fileStream = new FileStream(filePath, FileMode.Create))
                {
                    await movie.ImageFile.CopyToAsync(fileStream);
                }

                imageUrl = "/images/movies/" + uniqueFileName;
            }

            // Update movie in database with new data and image URL
            await _service.UpdateMovieAsync(movie, imageUrl);
            return RedirectToAction(nameof(Index));
        }

        // GET: Movies/Delete/1
        [Authorize(Roles = UserRoles.Admin)]
        public async Task<IActionResult> Delete(int id)
        {
            var movieDetails = await _service.GetMovieByIdAsync(id);
            if (movieDetails == null) return View("NotFound");
            return View(movieDetails);
        }

        // POST: Movies/Delete/1
        [HttpPost, ActionName("Delete")]
        [ValidateAntiForgeryToken]
        [Authorize(Roles = UserRoles.Admin)]
        public async Task<IActionResult> DeleteConfirmed(int id)
        {
            // Delete movie from database
            await _service.DeleteMovieAsync(id);
            return RedirectToAction(nameof(Index));
        }
    }
}