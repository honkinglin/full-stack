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
    public class CinemasController : Controller
    {
        private readonly ICinemasService _service;
        private readonly IWebHostEnvironment _webHostEnvironment;
        private readonly string[] _allowedExtensions = { ".jpg", ".jpeg", ".png", ".gif" };
        private readonly long _maxFileSize = 5 * 1024 * 1024; // 5MB

        public CinemasController(ICinemasService service, IWebHostEnvironment webHostEnvironment)
        {
            _service = service;
            _webHostEnvironment = webHostEnvironment;
        }

        [AllowAnonymous]
        public async Task<IActionResult> Index()
        {
            var allCinemas = await _service.GetAllAsync();
            return View(allCinemas);
        }

        // GET: Cinemas/Create
        public IActionResult Create()
        {
            return View(new CinemaVM());
        }

        // POST: Cinemas/Create
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> Create(CinemaVM cinema)
        {
            // Validate image file
            if (cinema.LogoFile == null || cinema.LogoFile.Length == 0)
            {
                ModelState.AddModelError("LogoFile", "Cinema logo is required");
            }
            else
            {
                // Check file size
                if (cinema.LogoFile.Length > _maxFileSize)
                {
                    ModelState.AddModelError("LogoFile", "File size cannot exceed 5MB");
                }

                // Check file extension
                var extension = Path.GetExtension(cinema.LogoFile.FileName).ToLowerInvariant();
                if (!_allowedExtensions.Contains(extension))
                {
                    ModelState.AddModelError("LogoFile", "Only JPG, JPEG, PNG, and GIF files are allowed");
                }
            }

            if (!ModelState.IsValid)
            {
                return View(cinema);
            }

            try
            {
                string uploadsFolder = Path.Combine(_webHostEnvironment.WebRootPath, "images/cinemas");
                if (!Directory.Exists(uploadsFolder))
                {
                    Directory.CreateDirectory(uploadsFolder);
                }

                string uniqueFileName = Guid.NewGuid().ToString() + "_" + cinema.LogoFile.FileName;
                string filePath = Path.Combine(uploadsFolder, uniqueFileName);

                using (var fileStream = new FileStream(filePath, FileMode.Create))
                {
                    await cinema.LogoFile.CopyToAsync(fileStream);
                }

                var cinemaEntity = new Cinema
                {
                    Name = cinema.Name,
                    Description = cinema.Description,
                    Logo = "/images/cinemas/" + uniqueFileName
                };

                await _service.AddAsync(cinemaEntity);
                return RedirectToAction(nameof(Index));
            }
            catch (Exception ex)
            {
                ModelState.AddModelError("", $"An error occurred while creating the cinema: {ex.Message}");
                return View(cinema);
            }
        }

        // GET: Cinemas/Edit/1
        public async Task<IActionResult> Edit(int id)
        {
            var cinemaDetails = await _service.GetByIdAsync(id);
            if (cinemaDetails == null) return View("NotFound");

            var response = new CinemaVM
            {
                Id = cinemaDetails.Id,
                Name = cinemaDetails.Name,
                Description = cinemaDetails.Description,
                ExistingLogo = cinemaDetails.Logo
            };

            ViewBag.CurrentImage = cinemaDetails.Logo;
            return View(response);
        }

        // POST: Cinemas/Edit/1
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> Edit(int id, CinemaVM cinema)
        {
            if (id != cinema.Id) return View("NotFound");

            // Validate image only if a new file is uploaded
            if (cinema.LogoFile != null && cinema.LogoFile.Length > 0)
            {
                if (cinema.LogoFile.Length > _maxFileSize)
                {
                    ModelState.AddModelError("LogoFile", "File size cannot exceed 5MB");
                }

                var extension = Path.GetExtension(cinema.LogoFile.FileName).ToLowerInvariant();
                if (!_allowedExtensions.Contains(extension))
                {
                    ModelState.AddModelError("LogoFile", "Only JPG, JPEG, PNG, and GIF files are allowed");
                }
            }

            if (!ModelState.IsValid)
            {
                ViewBag.CurrentImage = cinema.ExistingLogo;
                return View(cinema);
            }

            try
            {
                var existingCinema = await _service.GetByIdAsync(id);
                if (existingCinema == null) return View("NotFound");

                string logoUrl = existingCinema.Logo;

                if (cinema.LogoFile != null && cinema.LogoFile.Length > 0)
                {
                    // Delete old image if it exists
                    if (!string.IsNullOrEmpty(existingCinema.Logo))
                    {
                        var oldFilePath = Path.Combine(_webHostEnvironment.WebRootPath, existingCinema.Logo.TrimStart('/'));
                        if (System.IO.File.Exists(oldFilePath))
                        {
                            System.IO.File.Delete(oldFilePath);
                        }
                    }

                    string uploadsFolder = Path.Combine(_webHostEnvironment.WebRootPath, "images/cinemas");
                    if (!Directory.Exists(uploadsFolder))
                    {
                        Directory.CreateDirectory(uploadsFolder);
                    }

                    string uniqueFileName = Guid.NewGuid().ToString() + "_" + cinema.LogoFile.FileName;
                    string filePath = Path.Combine(uploadsFolder, uniqueFileName);

                    using (var fileStream = new FileStream(filePath, FileMode.Create))
                    {
                        await cinema.LogoFile.CopyToAsync(fileStream);
                    }

                    logoUrl = "/images/cinemas/" + uniqueFileName;
                }

                existingCinema.Name = cinema.Name;
                existingCinema.Description = cinema.Description;
                existingCinema.Logo = logoUrl;

                await _service.UpdateAsync(id, existingCinema);
                return RedirectToAction(nameof(Index));
            }
            catch (Exception ex)
            {
                ModelState.AddModelError("", $"An error occurred while updating the cinema: {ex.Message}");
                ViewBag.CurrentImage = cinema.ExistingLogo;
                return View(cinema);
            }
        }

        // GET: Cinemas/Details/1
        [AllowAnonymous]
        public async Task<IActionResult> Details(int id)
        {
            var cinemaDetails = await _service.GetByIdAsync(id);
            if (cinemaDetails == null) return View("NotFound");
            return View(cinemaDetails);
        }

        // GET: Cinemas/Delete/1
        public async Task<IActionResult> Delete(int id)
        {
            var cinemaDetails = await _service.GetByIdAsync(id);
            if (cinemaDetails == null) return View("NotFound");
            return View(cinemaDetails);
        }

        [HttpPost, ActionName("Delete")]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> DeleteConfirmed(int id)
        {
            var cinemaDetails = await _service.GetByIdAsync(id);
            if (cinemaDetails == null) return View("NotFound");

            // Delete associated image
            if (!string.IsNullOrEmpty(cinemaDetails.Logo))
            {
                var filePath = Path.Combine(_webHostEnvironment.WebRootPath, cinemaDetails.Logo.TrimStart('/'));
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