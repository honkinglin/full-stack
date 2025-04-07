using Microsoft.AspNetCore.Http;
using System.ComponentModel.DataAnnotations;

namespace eTickets.Models
{
    public class CinemaVM
    {
        public int Id { get; set; }

        [Required(ErrorMessage = "Cinema name is required")]
        [Display(Name = "Cinema Name")]
        public string Name { get; set; }

        [Required(ErrorMessage = "Cinema description is required")]
        [Display(Name = "Description")]
        public string Description { get; set; }

        [Display(Name = "Cinema Logo")]
        public IFormFile LogoFile { get; set; }

        public string ExistingLogo { get; set; }
    }
}