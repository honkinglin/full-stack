using eTickets.Data;
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
    [Authorize(Roles = UserRoles.Admin)]
    public class ProducersController : Controller
    {
        private readonly IProducersService _service;
        private readonly IWebHostEnvironment _webHostEnvironment;
        private readonly string[] _allowedExtensions = { ".jpg", ".jpeg", ".png", ".gif" };
        private readonly long _maxFileSize = 5 * 1024 * 1024; // 5MB

        public ProducersController(IProducersService service, IWebHostEnvironment webHostEnvironment)
        {
            _service = service;
            _webHostEnvironment = webHostEnvironment;
        }

        [AllowAnonymous]
        public async Task<IActionResult> Index()
        {
            var allProducers = await _service.GetAllAsync();
            return View(allProducers);
        }

        // GET: producers/details/1
        [AllowAnonymous]
        public async Task<IActionResult> Details(int id)
        {
            var producerDetails = await _service.GetByIdAsync(id);
            if (producerDetails == null) return View("NotFound");
            return View(producerDetails);
        }

        // GET: producers/create
        public IActionResult Create()
        {
            return View(new ProducerVM());
        }

        // POST: producers/create
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> Create(ProducerVM producer)
        {
            // Validate image file
            if (producer.ProfilePictureFile == null || producer.ProfilePictureFile.Length == 0)
            {
                ModelState.AddModelError("ProfilePictureFile", "Profile picture is required");
            }
            else
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

            if (!ModelState.IsValid)
            {
                return View(producer);
            }

            try
            {
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

                var producerEntity = new Producer
                {
                    FullName = producer.FullName,
                    Bio = producer.Bio,
                    ProfilePictureURL = "/images/producers/" + uniqueFileName
                };

                await _service.AddAsync(producerEntity);
                return RedirectToAction(nameof(Index));
            }
            catch (Exception ex)
            {
                ModelState.AddModelError("", $"An error occurred while creating the producer: {ex.Message}");
                return View(producer);
            }
        }

        // GET: producers/edit/1
        public async Task<IActionResult> Edit(int id)
        {
            var producerDetails = await _service.GetByIdAsync(id);
            if (producerDetails == null) return View("NotFound");

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
            if (id != producer.Id) return View("NotFound");

            // Validate image only if a new file is uploaded
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

            if (!ModelState.IsValid)
            {
                return View(producer);
            }

            try
            {
                var existingProducer = await _service.GetByIdAsync(id);
                if (existingProducer == null) return View("NotFound");

                string profilePictureUrl = existingProducer.ProfilePictureURL;

                if (producer.ProfilePictureFile != null && producer.ProfilePictureFile.Length > 0)
                {
                    // Delete old image if it exists
                    if (!string.IsNullOrEmpty(existingProducer.ProfilePictureURL))
                    {
                        var oldFilePath = Path.Combine(_webHostEnvironment.WebRootPath, existingProducer.ProfilePictureURL.TrimStart('/'));
                        if (System.IO.File.Exists(oldFilePath))
                        {
                            System.IO.File.Delete(oldFilePath);
                        }
                    }

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

                existingProducer.FullName = producer.FullName;
                existingProducer.Bio = producer.Bio;
                existingProducer.ProfilePictureURL = profilePictureUrl;

                await _service.UpdateAsync(id, existingProducer);
                return RedirectToAction(nameof(Index));
            }
            catch (Exception ex)
            {
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

            // Delete associated image
            if (!string.IsNullOrEmpty(producerDetails.ProfilePictureURL))
            {
                var filePath = Path.Combine(_webHostEnvironment.WebRootPath, producerDetails.ProfilePictureURL.TrimStart('/'));
                if (System.IO.File.Exists(filePath))
                {
                    System.IO.File.Delete(filePath);
                }
            }

            await _service.DeleteAsync(id);
            return RedirectToAction(nameof(Index));
        }
    }
}