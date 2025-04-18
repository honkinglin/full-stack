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
    // Restrict access to Admin role for this controller
    [Authorize(Roles = UserRoles.Admin)]
    public class ProducersController : Controller
    {
        private readonly IProducersService _service;
        private readonly IWebHostEnvironment _webHostEnvironment;
        // Define allowed image extensions and max file size (5MB)
        private readonly string[] _allowedExtensions = { ".jpg", ".jpeg", ".png", ".gif" };
        private readonly long _maxFileSize = 5 * 1024 * 1024; // 5MB

        // Constructor with dependency injection for service and hosting environment
        public ProducersController(IProducersService service, IWebHostEnvironment webHostEnvironment)
        {
            _service = service;
            _webHostEnvironment = webHostEnvironment;
        }

        // Allow anonymous access to view all producers
        [AllowAnonymous]
        public async Task<IActionResult> Index()
        {
            var allProducers = await _service.GetAllAsync();
            return View(allProducers);
        }

        // GET: producers/details/1
        // Allow anonymous access to view producer details
        [AllowAnonymous]
        public async Task<IActionResult> Details(int id)
        {
            var producerDetails = await _service.GetByIdAsync(id);
            // Return NotFound view if producer doesn't exist
            if (producerDetails == null) return View("NotFound");
            return View(producerDetails);
        }

        // GET: producers/create
        public IActionResult Create()
        {
            return View(new ProducerVM()); // Initialize view model for create form
        }

        // POST: producers/create
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> Create(ProducerVM producer)
        {
            // Validate uploaded profile picture file
            if (producer.ProfilePictureFile == null || producer.ProfilePictureFile.Length == 0)
            {
                ModelState.AddModelError("ProfilePictureFile", "Profile picture is required");
            }
            else
            {
                // Check file size limit
                if (producer.ProfilePictureFile.Length > _maxFileSize)
                {
                    ModelState.AddModelError("ProfilePictureFile", "File size cannot exceed 5MB");
                }

                // Validate file extension
                var extension = Path.GetExtension(producer.ProfilePictureFile.FileName).ToLowerInvariant();
                if (!_allowedExtensions.Contains(extension))
                {
                    ModelState.AddModelError("ProfilePictureFile", "Only JPG, JPEG, PNG, and GIF files are allowed");
                }
            }

            // Return to view with validation errors if model state is invalid
            if (!ModelState.IsValid)
            {
                return View(producer);
            }

            try
            {
                // Set up directory for storing producer profile pictures
                string uploadsFolder = Path.Combine(_webHostEnvironment.WebRootPath, "images/producers");
                if (!Directory.Exists(uploadsFolder))
                {
                    Directory.CreateDirectory(uploadsFolder);
                }

                // Generate unique filename to prevent conflicts
                string uniqueFileName = Guid.NewGuid().ToString() + "_" + producer.ProfilePictureFile.FileName;
                string filePath = Path.Combine(uploadsFolder, uniqueFileName);

                // Save uploaded file to server
                using (var fileStream = new FileStream(filePath, FileMode.Create))
                {
                    await producer.ProfilePictureFile.CopyToAsync(fileStream);
                }

                // Create new Producer entity with form data and image path
                var producerEntity = new Producer
                {
                    FullName = producer.FullName,
                    Bio = producer.Bio,
                    ProfilePictureURL = "/images/producers/" + uniqueFileName
                };

                // Save producer to database
                await _service.AddAsync(producerEntity);
                return RedirectToAction(nameof(Index));
            }
            catch (Exception ex)
            {
                // Handle errors during file upload or database operation
                ModelState.AddModelError("", $"An error occurred while creating the producer: {ex.Message}");
                return View(producer);
            }
        }

        // GET: producers/edit/1
        public async Task<IActionResult> Edit(int id)
        {
            var producerDetails = await _service.GetByIdAsync(id);
            if (producerDetails == null) return View("NotFound");

            // Map producer data to view model for editing
            var response = new ProducerVM
            {
                Id = producerDetails.Id,
                FullName = producerDetails.FullName,
                Bio = producerDetails.Bio,
                ExistingProfilePicture = producerDetails.ProfilePictureURL
            };

            return View(response);
        }

        // POST: producers/edit/1
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> Edit(int id, ProducerVM producer)
        {
            // Verify ID consistency
            if (id != producer.Id) return View("NotFound");

            // Validate new image if uploaded
            if (producer.ProfilePictureFile != null && producer.ProfilePictureFile.Length > 0)
            {
                if (producer.ProfilePictureFile.Length > _maxFileSize)
                {
                    ModelState.AddModelError("ProfilePictureFile", "File size cannot exceed 5MB");
                }

                var extension = Path.GetExtension(producer.ProfilePictureFile.FileName).ToLowerInvariant();
                if (!_allowedExtensions.Contains(extension))
                {
                    ModelState.AddModelError("ProfilePictureFile", "Only JPG, JPEG, PNG, and GIF files are allowed");
                }
            }

            // Return to view with validation errors if model state is invalid
            if (!ModelState.IsValid)
            {
                return View(producer);
            }

            try
            {
                // Retrieve existing producer from database
                var existingProducer = await _service.GetByIdAsync(id);
                if (existingProducer == null) return View("NotFound");

                string profilePictureUrl = existingProducer.ProfilePictureURL;

                // Handle new image upload if provided
                if (producer.ProfilePictureFile != null && producer.ProfilePictureFile.Length > 0)
                {
                    // Delete old image file if it exists
                    if (!string.IsNullOrEmpty(existingProducer.ProfilePictureURL))
                    {
                        var oldFilePath = Path.Combine(_webHostEnvironment.WebRootPath, existingProducer.ProfilePictureURL.TrimStart('/'));
                        if (System.IO.File.Exists(oldFilePath))
                        {
                            System.IO.File.Delete(oldFilePath);
                        }
                    }

                    // Save new image
                    string uploadsFolder = Path.Combine(_webHostEnvironment.WebRootPath, "images/producers");
                    if (!Directory.Exists(uploadsFolder))
                    {
                        Directory.CreateDirectory(uploadsFolder);
                    }

                    string uniqueFileName = Guid.NewGuid().ToString() + "_" + producer.ProfilePictureFile.FileName;
                    string filePath = Path.Combine(uploadsFolder, uniqueFileName);

                    using (var fileStream = new FileStream(filePath, FileMode.Create))
                    {
                        await producer.ProfilePictureFile.CopyToAsync(fileStream);
                    }

                    profilePictureUrl = "/images/producers/" + uniqueFileName;
                }

                // Update producer properties
                existingProducer.FullName = producer.FullName;
                existingProducer.Bio = producer.Bio;
                existingProducer.ProfilePictureURL = profilePictureUrl;

                // Save changes to database
                await _service.UpdateAsync(id, existingProducer);
                return RedirectToAction(nameof(Index));
            }
            catch (Exception ex)
            {
                // Handle errors during update
                ModelState.AddModelError("", $"An error occurred while updating the producer: {ex.Message}");
                return View(producer);
            }
        }

        // GET: producers/delete/1
        public async Task<IActionResult> Delete(int id)
        {
            var producerDetails = await _service.GetByIdAsync(id);
            if (producerDetails == null) return View("NotFound");
            return View(producerDetails);
        }

        // POST: producers/delete/1
        [HttpPost, ActionName("Delete")]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> DeleteConfirmed(int id)
        {
            var producerDetails = await _service.GetByIdAsync(id);
            if (producerDetails == null) return View("NotFound");

            // Delete associated image file
            if (!string.IsNullOrEmpty(producerDetails.ProfilePictureURL))
            {
                var filePath = Path.Combine(_webHostEnvironment.WebRootPath, producerDetails.ProfilePictureURL.TrimStart('/'));
                if (System.IO.File.Exists(filePath))
                {
                    System.IO.File.Delete(filePath);
                }
            }

            // Remove producer from database
            await _service.DeleteAsync(id);
            return RedirectToAction(nameof(Index));
        }
    }
}