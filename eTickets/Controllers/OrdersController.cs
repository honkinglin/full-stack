using eTickets.Data.Cart;
using eTickets.Data.Services;
using eTickets.Data.ViewModels;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using System.Security.Claims;
using System.Threading.Tasks;

namespace eTickets.Controllers
{
    // This controller handles operations related to orders and the shopping cart.
    // The [Authorize] attribute ensures that only authenticated users can access these actions.
    [Authorize]
    public class OrdersController : Controller
    {
        // Dependencies for managing movies, shopping cart, and orders.
        private readonly IMoviesService _moviesService;
        private readonly ShoppingCart _shoppingCart;
        private readonly IOrdersService _ordersService;

        // Constructor to inject dependencies.
        public OrdersController(IMoviesService moviesService, ShoppingCart shoppingCart, IOrdersService ordersService)
        {
            _moviesService = moviesService;
            _shoppingCart = shoppingCart;
            _ordersService = ordersService;
        }

        // Displays the list of orders for the current user.
        public async Task<IActionResult> Index()
        {
            // Retrieve the user's ID and role from the claims.
            string userId = User.FindFirstValue(ClaimTypes.NameIdentifier);
            string userRole = User.FindFirstValue(ClaimTypes.Role);

            // Fetch the orders based on the user's ID and role.
            var orders = await _ordersService.GetOrdersByUserIdAndRoleAsync(userId, userRole);

            // Pass the orders to the view for display.
            return View(orders);
        }

        // Displays the shopping cart page.
        public IActionResult ShoppingCart()
        {
            // Retrieve the items in the shopping cart.
            var items = _shoppingCart.GetShoppingCartItems();
            _shoppingCart.ShoppingCartItems = items;

            // Create a ViewModel to pass the shopping cart and total price to the view.
            var response = new ShoppingCartVM()
            {
                ShoppingCart = _shoppingCart,
                ShoppingCartTotal = _shoppingCart.GetShoppingCartTotal()
            };

            // Pass the ViewModel to the view for rendering.
            return View(response);
        }

        // Adds a movie to the shopping cart.
        public async Task<IActionResult> AddItemToShoppingCart(int id)
        {
            // Retrieve the movie by its ID.
            var item = await _moviesService.GetMovieByIdAsync(id);

            // If the movie exists, add it to the shopping cart.
            if (item != null)
            {
                _shoppingCart.AddItemToCart(item);
            }

            // Redirect to the shopping cart page.
            return RedirectToAction(nameof(ShoppingCart));
        }

        // Removes a movie from the shopping cart.
        public async Task<IActionResult> RemoveItemFromShoppingCart(int id)
        {
            // Retrieve the movie by its ID.
            var item = await _moviesService.GetMovieByIdAsync(id);

            // If the movie exists, remove it from the shopping cart.
            if (item != null)
            {
                _shoppingCart.RemoveItemFromCart(item);
            }

            // Redirect to the shopping cart page.
            return RedirectToAction(nameof(ShoppingCart));
        }

        // Completes the order by storing it in the database and clearing the shopping cart.
        public async Task<IActionResult> CompleteOrder()
        {
            // Retrieve the items in the shopping cart.
            var items = _shoppingCart.GetShoppingCartItems();

            // Retrieve the user's ID and email address from the claims.
            string userId = User.FindFirstValue(ClaimTypes.NameIdentifier);
            string userEmailAddress = User.FindFirstValue(ClaimTypes.Email);

            // Store the order in the database.
            await _ordersService.StoreOrderAsync(items, userId, userEmailAddress);

            // Clear the shopping cart after the order is completed.
            await _shoppingCart.ClearShoppingCartAsync();

            // Redirect to the "Order Completed" page.
            return View("OrderCompleted");
        }
    }
}
