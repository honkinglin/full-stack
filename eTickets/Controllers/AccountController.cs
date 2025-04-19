using eTickets.Data;
using eTickets.Data.Static;
using eTickets.Data.ViewModels;
using eTickets.Models;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using System;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.Extensions.Logging;

namespace eTickets.Controllers
{
    // This controller handles user account-related operations such as login, registration, and logout.
    public class AccountController : Controller
    {
        // Dependencies for user management, sign-in management, database context, and logging.
        private readonly UserManager<ApplicationUser> _userManager;
        private readonly SignInManager<ApplicationUser> _signInManager;
        private readonly AppDbContext _context;
        private readonly ILogger<AccountController> _logger;

        // Constructor to inject dependencies.
        public AccountController(UserManager<ApplicationUser> userManager, SignInManager<ApplicationUser> signInManager, AppDbContext context, ILogger<AccountController> logger)
        {
            _userManager = userManager;
            _signInManager = signInManager;
            _context = context;
            _logger = logger;
        }

        // Retrieves a list of all users in the system.
        public async Task<IActionResult> Users()
        {
            var users = await _context.Users.ToListAsync(); // Fetch all users from the database.
            return View(users); // Pass the list of users to the view.
        }

        // Displays the login page with an empty LoginVM model.
        public IActionResult Login() => View(new LoginVM());

        // Handles the login process.
        [HttpPost]
        public async Task<IActionResult> Login(LoginVM loginVM)
        {
            // Validate the model state.
            if (!ModelState.IsValid) return View(loginVM);

            // Find the user by their email address.
            var user = await _userManager.FindByEmailAsync(loginVM.EmailAddress);
            if (user != null)
            {
                // Check if the provided password is correct.
                var passwordCheck = await _userManager.CheckPasswordAsync(user, loginVM.Password);
                if (passwordCheck)
                {
                    // Sign in the user if the password is correct.
                    var result = await _signInManager.PasswordSignInAsync(user, loginVM.Password, false, false);
                    if (result.Succeeded)
                    {
                        return RedirectToAction("Index", "Movies"); // Redirect to the Movies page on success.
                    }
                }
                TempData["Error"] = "Wrong credentials. Please, try again!";
                return View(loginVM); // Return to the login page with an error message.
            }

            TempData["Error"] = "Wrong credentials. Please, try again!";
            return View(loginVM); // Return to the login page with an error message.
        }

        // Displays the registration page with an empty RegisterVM model.
        public IActionResult Register() => View(new RegisterVM());

        // Handles the registration process.
        [HttpPost]
        public async Task<IActionResult> Register(RegisterVM registerVM)
        {
            _logger.LogInformation("Starting registration for {Email}", registerVM.EmailAddress);

            // Validate the model state.
            if (!ModelState.IsValid)
            {
                _logger.LogWarning("Model state invalid for {Email}", registerVM.EmailAddress);
                return View(registerVM);
            }

            // Check if a user with the provided email already exists.
            var user = await _userManager.FindByEmailAsync(registerVM.EmailAddress);
            if (user != null)
            {
                TempData["Error"] = "This email address is already in use";
                _logger.LogInformation("Email {Email} already in use", registerVM.EmailAddress);
                return View(registerVM);
            }

            // Create a new ApplicationUser instance.
            var newUser = new ApplicationUser()
            {
                FullName = registerVM.FullName,
                Email = registerVM.EmailAddress,
                UserName = registerVM.UserName,
            };

            _logger.LogInformation("Attempting to create user for {Email}", registerVM.EmailAddress);
            // Attempt to create the user with the provided password.
            var newUserResponse = await _userManager.CreateAsync(newUser, registerVM.Password);

            if (newUserResponse.Succeeded)
            {
                _logger.LogInformation("User created successfully for {Email}", registerVM.EmailAddress);

                // Assign the user to the "User" role.
                await _userManager.AddToRoleAsync(newUser, UserRoles.User);
                _logger.LogInformation("Role {Role} assigned to {Email}", UserRoles.User, registerVM.EmailAddress);

                try
                {
                    // Save changes to the database.
                    await _context.SaveChangesAsync();
                    _logger.LogInformation("Changes saved to database for {Email}", registerVM.EmailAddress);
                }
                catch (Exception ex)
                {
                    _logger.LogError(ex, "Failed to save changes to database for {Email}", registerVM.EmailAddress);
                    TempData["Error"] = "An error occurred while saving your account.";
                    return View(registerVM);
                }

                // Verify that the user was successfully created in the database.
                var createdUser = await _userManager.FindByEmailAsync(registerVM.EmailAddress);
                if (createdUser == null)
                {
                    _logger.LogError("User not found in database after creation for {Email}", registerVM.EmailAddress);
                    TempData["Error"] = "User creation succeeded but not found in database.";
                    return View(registerVM);
                }
            }
            else
            {
                // Log and display errors if user creation fails.
                var errors = string.Join(", ", newUserResponse.Errors.Select(e => e.Description));
                _logger.LogError("User creation failed for {Email}: {Errors}", registerVM.EmailAddress, errors);
                TempData["Error"] = $"Failed to create user: {errors}";
                return View(registerVM);
            }

            _logger.LogInformation("Redirecting to RegisterCompleted for {Email}", registerVM.EmailAddress);
            return View("RegisterCompleted"); // Redirect to a registration completion page.
        }

        // Logs out the currently signed-in user.
        [HttpPost]
        public async Task<IActionResult> Logout()
        {
            await _signInManager.SignOutAsync(); // Sign out the user.
            return RedirectToAction("Index", "Movies"); // Redirect to the Movies page.
        }

        // Displays the Access Denied page when a user tries to access a restricted resource.
        public IActionResult AccessDenied(string ReturnUrl)
        {
            return View();
        }
    }
}
