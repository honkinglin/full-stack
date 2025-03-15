using eTickets.Data;
using eTickets.Data.Static;
using eTickets.Data.ViewModels;
using eTickets.Models;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.Extensions.Logging;

namespace eTickets.Controllers
{
    public class AccountController : Controller
    {
        private readonly UserManager<ApplicationUser> _userManager;
        private readonly SignInManager<ApplicationUser> _signInManager;
        private readonly AppDbContext _context;
        private readonly ILogger<AccountController> _logger;

        public AccountController(UserManager<ApplicationUser> userManager, SignInManager<ApplicationUser> signInManager, AppDbContext context, ILogger<AccountController> logger)
        {
            _userManager = userManager;
            _signInManager = signInManager;
            _context = context;
            _logger = logger;
        }


        public async Task<IActionResult> Users()
        {
            var users = await _context.Users.ToListAsync();
            return View(users);
        }


        public IActionResult Login() => View(new LoginVM());

        [HttpPost]
        public async Task<IActionResult> Login(LoginVM loginVM)
        {
            if (!ModelState.IsValid) return View(loginVM);

            var user = await _userManager.FindByEmailAsync(loginVM.EmailAddress);
            if(user != null)
            {
                var passwordCheck = await _userManager.CheckPasswordAsync(user, loginVM.Password);
                if (passwordCheck)
                {
                    var result = await _signInManager.PasswordSignInAsync(user, loginVM.Password, false, false);
                    if (result.Succeeded)
                    {
                        return RedirectToAction("Index", "Movies");
                    }
                }
                TempData["Error"] = "Wrong credentials. Please, try again!";
                return View(loginVM);
            }

            TempData["Error"] = "Wrong credentials. Please, try again!";
            return View(loginVM);
        }


        public IActionResult Register() => View(new RegisterVM());

[HttpPost]
        public async Task<IActionResult> Register(RegisterVM registerVM)
        {
            _logger.LogInformation("Starting registration for {Email}", registerVM.EmailAddress);

            if (!ModelState.IsValid)
            {
                _logger.LogWarning("Model state invalid for {Email}", registerVM.EmailAddress);
                return View(registerVM);
            }

            var user = await _userManager.FindByEmailAsync(registerVM.EmailAddress);
            if (user != null)
            {
                TempData["Error"] = "This email address is already in use";
                _logger.LogInformation("Email {Email} already in use", registerVM.EmailAddress);
                return View(registerVM);
            }

            var newUser = new ApplicationUser()
            {
                FullName = registerVM.FullName,
                Email = registerVM.EmailAddress,
                UserName = registerVM.EmailAddress
            };

            _logger.LogInformation("Attempting to create user for {Email}", registerVM.EmailAddress);
            var newUserResponse = await _userManager.CreateAsync(newUser, registerVM.Password);

            if (newUserResponse.Succeeded)
            {
                _logger.LogInformation("User created successfully for {Email}", registerVM.EmailAddress);

                // 添加角色
                await _userManager.AddToRoleAsync(newUser, UserRoles.User);
                _logger.LogInformation("Role {Role} assigned to {Email}", UserRoles.User, registerVM.EmailAddress);

                // 强制保存更改到 SQLite
                try
                {
                    await _context.SaveChangesAsync();
                    _logger.LogInformation("Changes saved to database for {Email}", registerVM.EmailAddress);
                }
                catch (Exception ex)
                {
                    _logger.LogError(ex, "Failed to save changes to database for {Email}", registerVM.EmailAddress);
                    TempData["Error"] = "An error occurred while saving your account.";
                    return View(registerVM);
                }

                // 验证用户是否真的写入数据库
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
                var errors = string.Join(", ", newUserResponse.Errors.Select(e => e.Description));
                _logger.LogError("User creation failed for {Email}: {Errors}", registerVM.EmailAddress, errors);
                TempData["Error"] = $"Failed to create user: {errors}";
                return View(registerVM);
            }

            _logger.LogInformation("Redirecting to RegisterCompleted for {Email}", registerVM.EmailAddress);
            return View("RegisterCompleted");
        }

        [HttpPost]
        public async Task<IActionResult> Logout()
        {
            await _signInManager.SignOutAsync();
            return RedirectToAction("Index", "Movies");
        }

        public IActionResult AccessDenied(string ReturnUrl)
        {
            return View();
        }

    }
}
