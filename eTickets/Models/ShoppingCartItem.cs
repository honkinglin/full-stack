using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace eTickets.Models
{
    public class ShoppingCartItem
    {
        // Primary key for the ShoppingCartItem entity.
        [Key]
        public int Id { get; set; }

        // Navigation property to the Movie entity.
        // Represents the movie associated with this shopping cart item.
        public Movie Movie { get; set; }

        // The quantity of the movie in the shopping cart.
        public int Amount { get; set; }

        // The unique identifier for the shopping cart.
        // This is used to group items belonging to the same cart.
        public string ShoppingCartId { get; set; }

        // The ID of the user associated with this shopping cart item.
        // This is optional and used to associate the cart item with a specific user.
        public string ApplicationUserId { get; set; }

        // Navigation property to the ApplicationUser entity.
        // Represents the user associated with this shopping cart item.
        [ForeignKey("ApplicationUserId")]
        public ApplicationUser ApplicationUser { get; set; }
    }
}
