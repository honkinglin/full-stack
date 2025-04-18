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
    public class ShoppingCart
    {
        private readonly AppDbContext _context;
        private readonly UserManager<ApplicationUser> _userManager;
        private readonly IHttpContextAccessor _httpContextAccessor;

        public string ShoppingCartId { get; set; }
        public List<ShoppingCartItem> ShoppingCartItems { get; set; }

        public ShoppingCart(AppDbContext context, UserManager<ApplicationUser> userManager, IHttpContextAccessor httpContextAccessor)
        {
            _context = context;
            _userManager = userManager;
            _httpContextAccessor = httpContextAccessor;
        }

        public static ShoppingCart GetShoppingCart(IServiceProvider services)
        {
            var httpContextAccessor = services.GetRequiredService<IHttpContextAccessor>();
            var context = services.GetRequiredService<AppDbContext>();

            var userManager = services.GetRequiredService<UserManager<ApplicationUser>>();
            var user = httpContextAccessor.HttpContext.User;
            string cartId;
            var session = httpContextAccessor.HttpContext.Session;
            if (user.Identity.IsAuthenticated)
            {
                var currentUser = userManager.GetUserAsync(user).GetAwaiter().GetResult();
                cartId = currentUser?.Id ?? Guid.NewGuid().ToString(); // Use user ID for logged-in users
            }
            else
            {
                cartId = session.GetString("CartId") ?? Guid.NewGuid().ToString();
                session.SetString("CartId", cartId); // Session for anonymous users
            }
            return new ShoppingCart(context, userManager, httpContextAccessor) { ShoppingCartId = cartId };
        }

        public void AddItemToCart(Movie movie)
        {
            var shoppingCartItem = _context.ShoppingCartItems.FirstOrDefault(n => n.Movie.Id == movie.Id && n.ShoppingCartId == ShoppingCartId);
            string? userId = _httpContextAccessor.HttpContext.User.Identity.IsAuthenticated
                            ? _userManager.GetUserId(_httpContextAccessor.HttpContext.User)
                            : null;
            if (shoppingCartItem == null)
            {
                shoppingCartItem = new ShoppingCartItem()
                {
                    ShoppingCartId = ShoppingCartId,
                    Movie = movie,
                    Amount = 1,
                    ApplicationUserId = userId // Set user ID
                };

                _context.ShoppingCartItems.Add(shoppingCartItem);
            }
            else
            {
                shoppingCartItem.Amount++;
            }
            _context.SaveChanges();
        }

        public void RemoveItemFromCart(Movie movie)
        {
            var shoppingCartItem = _context.ShoppingCartItems.FirstOrDefault(n => n.Movie.Id == movie.Id && n.ShoppingCartId == ShoppingCartId);

            if (shoppingCartItem != null)
            {
                if (shoppingCartItem.Amount > 1)
                {
                    shoppingCartItem.Amount--;
                }
                else
                {
                    _context.ShoppingCartItems.Remove(shoppingCartItem);
                }
            }
            _context.SaveChanges();
        }

        public List<ShoppingCartItem> GetShoppingCartItems()
        {
            if (_httpContextAccessor.HttpContext.User.Identity.IsAuthenticated)
            {
                var userId = _userManager.GetUserId(_httpContextAccessor.HttpContext.User);
                return ShoppingCartItems ?? (ShoppingCartItems = _context.ShoppingCartItems
                    .Where(n => n.ApplicationUserId == userId)
                    .Include(n => n.Movie)
                    .ToList());
            }
            return ShoppingCartItems ?? (ShoppingCartItems = _context.ShoppingCartItems.Where(n => n.ShoppingCartId == ShoppingCartId).Include(n => n.Movie).ToList());
        }

        public double GetShoppingCartTotal()
        {
            if (_httpContextAccessor.HttpContext.User.Identity.IsAuthenticated)
            {
                var userId = _userManager.GetUserId(_httpContextAccessor.HttpContext.User);
                return _context.ShoppingCartItems
                    .Where(n => n.ApplicationUserId == userId)
                    .Select(n => n.Movie.Price * n.Amount)
                    .Sum();
            }
            return _context.ShoppingCartItems
                .Where(n => n.ShoppingCartId == ShoppingCartId)
                .Select(n => n.Movie.Price * n.Amount)
                .Sum();
        }

        public async Task ClearShoppingCartAsync()
        {
            string? userId = _httpContextAccessor.HttpContext.User.Identity.IsAuthenticated
                ? _userManager.GetUserId(_httpContextAccessor.HttpContext.User)
                : null;

            var items = userId != null
                ? await _context.ShoppingCartItems
                    .Where(n => n.ApplicationUserId == userId)
                    .ToListAsync()
                : await _context.ShoppingCartItems
                    .Where(n => n.ShoppingCartId == ShoppingCartId)
                    .ToListAsync();

            _context.ShoppingCartItems.RemoveRange(items);
            await _context.SaveChangesAsync();
            ShoppingCartItems = null;

            // Clear session for anonymous users
            if (userId == null)
            {
                _httpContextAccessor.HttpContext.Session.Remove("CartId");
            }
        }
    }
}
