using eTickets.Models;
using Microsoft.AspNetCore.Http;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.DependencyInjection;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Identity;

namespace eTickets.Data.Cart
{
    // This class represents the shopping cart functionality for the application.
    public class ShoppingCart
    {
        // Dependencies for database context, user management, and HTTP context access.
        private readonly AppDbContext _context;
        private readonly UserManager<ApplicationUser> _userManager;
        private readonly IHttpContextAccessor _httpContextAccessor;

        // The unique identifier for the shopping cart.
        public string ShoppingCartId { get; set; }

        // A list of items in the shopping cart.
        public List<ShoppingCartItem> ShoppingCartItems { get; set; }

        // Constructor to inject dependencies.
        public ShoppingCart(AppDbContext context, UserManager<ApplicationUser> userManager, IHttpContextAccessor httpContextAccessor)
        {
            _context = context;
            _userManager = userManager;
            _httpContextAccessor = httpContextAccessor;
        }

        // Factory method to create or retrieve a shopping cart instance.
        public static ShoppingCart GetShoppingCart(IServiceProvider services)
        {
            // Retrieve required services.
            var httpContextAccessor = services.GetRequiredService<IHttpContextAccessor>();
            var context = services.GetRequiredService<AppDbContext>();
            var userManager = services.GetRequiredService<UserManager<ApplicationUser>>();

            // Determine the cart ID based on user authentication status.
            var user = httpContextAccessor.HttpContext.User;
            string cartId;
            var session = httpContextAccessor.HttpContext.Session;

            if (user.Identity.IsAuthenticated)
            {
                // Use the user's ID as the cart ID for authenticated users.
                var currentUser = userManager.GetUserAsync(user).GetAwaiter().GetResult();
                cartId = currentUser?.Id ?? Guid.NewGuid().ToString();
            }
            else
            {
                // Use a session-based cart ID for anonymous users.
                cartId = session.GetString("CartId") ?? Guid.NewGuid().ToString();
                session.SetString("CartId", cartId);
            }

            // Return a new ShoppingCart instance with the determined cart ID.
            return new ShoppingCart(context, userManager, httpContextAccessor) { ShoppingCartId = cartId };
        }

        // Adds a movie to the shopping cart.
        public void AddItemToCart(Movie movie)
        {
            // Check if the item already exists in the cart.
            var shoppingCartItem = _context.ShoppingCartItems.FirstOrDefault(n => n.Movie.Id == movie.Id && n.ShoppingCartId == ShoppingCartId);

            // Get the user ID if the user is authenticated.
            string? userId = _httpContextAccessor.HttpContext.User.Identity.IsAuthenticated
                            ? _userManager.GetUserId(_httpContextAccessor.HttpContext.User)
                            : null;

            if (shoppingCartItem == null)
            {
                // If the item does not exist, create a new shopping cart item.
                shoppingCartItem = new ShoppingCartItem()
                {
                    ShoppingCartId = ShoppingCartId,
                    Movie = movie,
                    Amount = 1,
                    ApplicationUserId = userId // Set user ID if available.
                };

                // Add the new item to the database.
                _context.ShoppingCartItems.Add(shoppingCartItem);
            }
            else
            {
                // If the item exists, increment its quantity.
                shoppingCartItem.Amount++;
            }

            // Save changes to the database.
            _context.SaveChanges();
        }

        // Removes a movie from the shopping cart.
        public void RemoveItemFromCart(Movie movie)
        {
            // Find the item in the shopping cart.
            var shoppingCartItem = _context.ShoppingCartItems.FirstOrDefault(n => n.Movie.Id == movie.Id && n.ShoppingCartId == ShoppingCartId);

            if (shoppingCartItem != null)
            {
                if (shoppingCartItem.Amount > 1)
                {
                    // Decrease the quantity if it is greater than 1.
                    shoppingCartItem.Amount--;
                }
                else
                {
                    // Remove the item from the cart if the quantity is 1.
                    _context.ShoppingCartItems.Remove(shoppingCartItem);
                }
            }

            // Save changes to the database.
            _context.SaveChanges();
        }

        // Retrieves the list of items in the shopping cart.
        public List<ShoppingCartItem> GetShoppingCartItems()
        {
            if (_httpContextAccessor.HttpContext.User.Identity.IsAuthenticated)
            {
                // For authenticated users, retrieve items based on the user ID.
                var userId = _userManager.GetUserId(_httpContextAccessor.HttpContext.User);
                return ShoppingCartItems ?? (ShoppingCartItems = _context.ShoppingCartItems
                    .Where(n => n.ApplicationUserId == userId)
                    .Include(n => n.Movie)
                    .ToList());
            }

            // For anonymous users, retrieve items based on the cart ID.
            return ShoppingCartItems ?? (ShoppingCartItems = _context.ShoppingCartItems
                .Where(n => n.ShoppingCartId == ShoppingCartId)
                .Include(n => n.Movie)
                .ToList());
        }

        // Calculates the total price of items in the shopping cart.
        public double GetShoppingCartTotal()
        {
            if (_httpContextAccessor.HttpContext.User.Identity.IsAuthenticated)
            {
                // For authenticated users, calculate the total based on the user ID.
                var userId = _userManager.GetUserId(_httpContextAccessor.HttpContext.User);
                return _context.ShoppingCartItems
                    .Where(n => n.ApplicationUserId == userId)
                    .Select(n => n.Movie.Price * n.Amount)
                    .Sum();
            }

            // For anonymous users, calculate the total based on the cart ID.
            return _context.ShoppingCartItems
                .Where(n => n.ShoppingCartId == ShoppingCartId)
                .Select(n => n.Movie.Price * n.Amount)
                .Sum();
        }

        // Clears all items from the shopping cart.
        public async Task ClearShoppingCartAsync()
        {
            // Determine the user ID if the user is authenticated.
            string? userId = _httpContextAccessor.HttpContext.User.Identity.IsAuthenticated
                ? _userManager.GetUserId(_httpContextAccessor.HttpContext.User)
                : null;

            // Retrieve items to be cleared based on the user ID or cart ID.
            var items = userId != null
                ? await _context.ShoppingCartItems
                    .Where(n => n.ApplicationUserId == userId)
                    .ToListAsync()
                : await _context.ShoppingCartItems
                    .Where(n => n.ShoppingCartId == ShoppingCartId)
                    .ToListAsync();

            // Remove the items from the database.
            _context.ShoppingCartItems.RemoveRange(items);
            await _context.SaveChangesAsync();

            // Clear the in-memory shopping cart items.
            ShoppingCartItems = null;

            // Clear the session for anonymous users.
            if (userId == null)
            {
                _httpContextAccessor.HttpContext.Session.Remove("CartId");
            }
        }
    }
}
