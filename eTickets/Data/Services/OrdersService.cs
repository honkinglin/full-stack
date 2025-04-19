using eTickets.Models;
using Microsoft.EntityFrameworkCore;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace eTickets.Data.Services
{
    // This service handles operations related to orders in the application.
    public class OrdersService : IOrdersService
    {
        // Dependency on the application's database context.
        private readonly AppDbContext _context;

        // Constructor to inject the database context dependency.
        public OrdersService(AppDbContext context)
        {
            _context = context;
        }

        // Retrieves a list of orders based on the user's ID and role.
        public async Task<List<Order>> GetOrdersByUserIdAndRoleAsync(string userId, string userRole)
        {
            // Fetch all orders, including related order items, movies, and user details.
            var orders = await _context.Orders
                .Include(n => n.OrderItems) // Include the order items for each order.
                .ThenInclude(n => n.Movie) // Include the movie details for each order item.
                .Include(n => n.User) // Include the user details for each order.
                .ToListAsync();

            // If the user is not an admin, filter the orders to only include those belonging to the user.
            if (userRole != "Admin")
            {
                orders = orders.Where(n => n.UserId == userId).ToList();
            }

            // Return the filtered or full list of orders.
            return orders;
        }

        // Stores a new order in the database based on the shopping cart items.
        public async Task StoreOrderAsync(List<ShoppingCartItem> items, string userId, string userEmailAddress)
        {
            // Create a new order with the user's ID and email address.
            var order = new Order()
            {
                UserId = userId, // The ID of the user placing the order.
                Email = userEmailAddress // The email address of the user.
            };

            // Add the new order to the database.
            await _context.Orders.AddAsync(order);
            await _context.SaveChangesAsync(); // Save changes to generate the order ID.

            // Loop through each shopping cart item to create corresponding order items.
            foreach (var item in items)
            {
                var orderItem = new OrderItem()
                {
                    Amount = item.Amount, // The quantity of the movie in the order.
                    MovieId = item.Movie.Id, // The ID of the movie being ordered.
                    OrderId = order.Id, // The ID of the order this item belongs to.
                    Price = item.Movie.Price // The price of the movie.
                };

                // Add the order item to the database.
                await _context.OrderItems.AddAsync(orderItem);
            }

            // Save all order items to the database.
            await _context.SaveChangesAsync();
        }
    }
}
