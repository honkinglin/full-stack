using eTickets.Data.Cart;
using Microsoft.AspNetCore.Mvc;

namespace eTickets.Data.ViewComponents
{
    // This class represents a ViewComponent for displaying a shopping cart summary.
    // ViewComponents are reusable components that render a portion of a view.
    public class ShoppingCartSummary : ViewComponent
    {
        // A private field to hold the ShoppingCart instance.
        private readonly ShoppingCart _shoppingCart;

        // Constructor to inject the ShoppingCart dependency.
        public ShoppingCartSummary(ShoppingCart shoppingCart)
        {
            _shoppingCart = shoppingCart; // Assign the injected ShoppingCart instance to the private field.
        }

        // The Invoke method is called when the ViewComponent is rendered.
        // It retrieves the shopping cart items and passes the count to the view.
        public IViewComponentResult Invoke()
        {
            // Retrieve the list of items in the shopping cart.
            var items = _shoppingCart.GetShoppingCartItems();

            // Pass the count of items in the shopping cart to the view.
            return View(items.Count);
        }
    }
}
