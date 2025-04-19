using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace eTickets.Models
{
    public class OrderItem
    {
        // Primary key for the OrderItem entity.
        [Key]
        public int Id { get; set; }

        // The quantity of the movie in the order.
        public int Amount { get; set; }

        // The price of the movie at the time of the order.
        public double Price { get; set; }

        // Foreign key to the Movie entity.
        public int MovieId { get; set; }

        // Navigation property to the Movie entity.
        // The [ForeignKey] attribute specifies that MovieId is the foreign key for this relationship.
        [ForeignKey("MovieId")]
        public Movie Movie { get; set; }

        // Foreign key to the Order entity.
        public int OrderId { get; set; }

        // Navigation property to the Order entity.
        // The [ForeignKey] attribute specifies that OrderId is the foreign key for this relationship.
        [ForeignKey("OrderId")]
        public Order Order { get; set; }
    }
}
