using eTickets.Data.Services;
using eTickets.Data.Static;
using eTickets.Models;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Hosting;
using System;
using System.IO;
using System.Linq;
using System.Threading.Tasks;

namespace eTickets.Controllers
{
    // Restrict access to Admin role only for this controller
    [Authorize(Roles = UserRoles.Admin)]
    public class ActorsController : Controller
    {
        private readonly IActorsService _service;
        private readonly IWebHostEnvironment _webHostEnvironment;
        // Define allowed image extensions and max file size (5MB)
        private readonly string[] _allowedExtensions = { ".jpg", ".jpeg", ".png", ".gif" };
        private readonly long _maxFileSize = 5 * 1024 * 1024; // 5MB

        // Constructor with dependency injection for service and hosting environment
        public ActorsController(IActorsService service, IWebHostEnvironment webHostEnvironment)
        {
            _service = service;
            _webHostEnvironment = webHostEnvironment;
        }

        // Allow anonymous access to view all actors
        [AllowAnonymous]
        public async Task<IActionResult> Index()
        {
            var data = await _service.GetAllAsync();
            return View(data);
        }

        // GET: Actors/Create
        public IActionResult Create()
        {
            return View(new ActorVM()); // Initialize view model for create form
        }

        // POST: Actors/Create
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> Create(ActorVM actor)
        {
            // Validate uploaded image file
            if (actor.ProfilePictureFile == null || actor.ProfilePictureFile.Length == 0)
            {
                ModelState.AddModelError("ProfilePictureFile", "Profile picture is required");
            }
            else
            {
                // Check file size limit
                if (actor.ProfilePictureFile.Length > _maxFileSize)
                {
                    ModelState.AddModelError("ProfilePictureFile", "File size cannot exceed 5MB");
                }

                // Validate file extension
                var extension = Path.GetExtension(actor.ProfilePictureFile.FileName).ToLowerInvariant();
                if (!_allowedExtensions.Contains(extension))
                {
                    ModelState.AddModelError("ProfilePictureFile", "Only JPG, JPEG, PNG, and GIF files are allowed");
                }
            }

            // Return to view with validation errors if model state is invalid
            if (!ModelState.IsValid)
            {
                return View(actor);
            }

            try
            {
                // Set up directory for storing actor images
                string uploadsFolder = Path.Combine(_webHostEnvironment.WebRootPath, "images/actors");
                if (!Directory.Exists(uploadsFolder))
                {
                    Directory.CreateDirectory(uploadsFolder);
                }

                // Generate unique filename to prevent conflicts
                string uniqueFileName = Guid.NewGuid().ToString() + "_" + actor.ProfilePictureFile.FileName;
                string filePath = Path.Combine(uploadsFolder, uniqueFileName);

                // Save uploaded file to server
                using (var fileStream = new FileStream(filePath, FileMode.Create))
                {
                    await actor.ProfilePictureFile.CopyToAsync(fileStream);
                }

                // Create new Actor entity with form data and image path
                var actorEntity = new Actor
                {
                    FullName = actor.FullName,
                    Bio = actor.Bio,
                    ProfilePictureURL = "/images/actors/" + uniqueFileName
                };

                // Save actor to database
                await _service.AddAsync(actorEntity);
                return RedirectToAction(nameof(Index));
            }
            catch (Exception ex)
            {
                // Handle any errors during file upload or database operation
                ModelState.AddModelError("", $"An error occurred while creating the actor: {ex.Message}");
                return View(actor);
            }
        }

        // GET: Actors/Details/1
        [AllowAnonymous]
        public async Task<IActionResult> Details(int id)
        {
            var actorDetails = await _service.GetByIdAsync(id);
            // Return NotFound view if actor doesn't exist
            if (actorDetails == null) return View("NotFound");
            return View(actorDetails);
        }

        // GET: Actors/Edit/1
        public async Task<IActionResult> Edit(int id)
        {
            var actorDetails = await _service.GetByIdAsync(id);
            if (actorDetails == null) return View("NotFound");

            // Map actor data to view model for editing
            var response = new ActorVM
            {
                Id = actorDetails.Id,
                FullName = actorDetails.FullName,
                Bio = actorDetails.Bio,
                ExistingProfilePicture = actorDetails.ProfilePictureURL
            };

            return View(response);
        }

        // POST: Actors/Edit/1
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> Edit(int id, ActorVM actor)
        {
            // Verify ID consistency
            if (id != actor.Id) return View("NotFound");

            // Validate new image if uploaded
            if (actor.ProfilePictureFile != null && actor.ProfilePictureFile.Length > 0)
            {
                if (actor.ProfilePictureFile.Length > _maxFileSize)
                {
                    ModelState.AddModelError("ProfilePictureFile", "File size cannot exceed 5MB");
                }

                var extension = Path.GetExtension(actor.ProfilePictureFile.FileName).ToLowerInvariant();
                if (!_allowedExtensions.Contains(extension))
                {
                    ModelState.AddModelError("ProfilePictureFile", "Only JPG, JPEG, PNG, and GIF files are allowed");
                }
            }

            if (!ModelState.IsValid)
            {
                return View(actor);
            }

            try
            {
                // Retrieve existing actor from database
                var existingActor = await _service.GetByIdAsync(id);
                if (existingActor == null) return View("NotFound");

                string profilePictureUrl = existingActor.ProfilePictureURL;

                // Handle new image upload if provided
                if (actor.ProfilePictureFile != null && actor.ProfilePictureFile.Length > 0)
                {
                    // Delete old image file if it exists
                    if (!string.IsNullOrEmpty(existingActor.ProfilePictureURL))
                    {
                        var oldFilePath = Path.Combine(_webHostEnvironment.WebRootPath, existingActor.ProfilePictureURL.TrimStart('/'));
                        if (System.IO.File.Exists(oldFilePath))
                        {
                            System.IO.File.Delete(oldFilePath);
                        }
                    }

                    // Save new image
                    string uploadsFolder = Path.Combine(_webHostEnvironment.WebRootPath, "images/actors");
                    if (!Directory.Exists(uploadsFolder))
                    {
                        Directory.CreateDirectory(uploadsFolder);
                    }

                    string uniqueFileName = Guid.NewGuid().ToString() + "_" + actor.ProfilePictureFile.FileName;
                    string filePath = Path.Combine(uploadsFolder, uniqueFileName);

                    using (var fileStream = new FileStream(filePath, FileMode.Create))
                    {
                        await actor.ProfilePictureFile.CopyToAsync(fileStream);
                    }

                    profilePictureUrl = "/images/actors/" + uniqueFileName;
                }

                // Update actor properties
                existingActor.FullName = actor.FullName;
                existingActor.Bio = actor.Bio;
                existingActor.ProfilePictureURL = profilePictureUrl;

                // Save changes to database
                await _service.UpdateAsync(id, existingActor);
                return RedirectToAction(nameof(Index));
            }
            catch (Exception ex)
            {
                // Handle errors during update
                ModelState.AddModelError("", $"An error occurred while updating the actor: {ex.Message}");
                return View(actor);
            }
        }

        // GET: Actors/Delete/1
        public async Task<IActionResult> Delete(int id)
        {
            var actorDetails = await _service.GetByIdAsync(id);
            if (actorDetails == null) return View("NotFound");
            return View(actorDetails);
        }

        // POST: Actors/Delete/1
        [HttpPost, ActionName("Delete")]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> DeleteConfirmed(int id)
        {
            var actorDetails = await _service.GetByIdAsync(id);
            if (actorDetails == null) return View("NotFound");

            // Delete associated image file
            if (!string.IsNullOrEmpty(actorDetails.ProfilePictureURL))
            {
                var filePath = Path.Combine(_webHostEnvironment.WebRootPath, actorDetails.ProfilePictureURL.TrimStart('/'));
                if (System.IO.File.Exists(filePath))
                {
                    System.IO.File.Delete(filePath);
                }
            }

            // Remove actor from database
            await _service.DeleteAsync(id);
            return RedirectToAction(nameof(Index));
        }
    }
}