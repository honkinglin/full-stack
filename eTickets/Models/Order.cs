using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace eTickets.Models
{
    public class Order
    {
        // Primary key for the Order entity.
        [Key]
        public int Id { get; set; }

        // The email address of the user who placed the order.
        public string Email { get; set; }

        // The ID of the user who placed the order.
        public string UserId { get; set; }

        // Navigation property to the ApplicationUser entity.
        // The [ForeignKey] attribute specifies that UserId is the foreign key for this relationship.
        [ForeignKey(nameof(UserId))]
        public ApplicationUser User { get; set; }

        // Navigation property to the list of OrderItems associated with this order.
        public List<OrderItem> OrderItems { get; set; }
    }
}
