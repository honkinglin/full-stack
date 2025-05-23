using Microsoft.AspNetCore.Http;
using System.ComponentModel.DataAnnotations;

namespace eTickets.Models
{
    public class ActorVM
    {
        public int Id { get; set; }

        [Display(Name = "Profile Picture")]
        public IFormFile ProfilePictureFile { get; set; }

        public string ExistingProfilePicture { get; set; }

        [Display(Name = "Full Name")]
        [Required(ErrorMessage = "Full Name is required")]
        [StringLength(50, MinimumLength = 3, ErrorMessage = "Full Name must be between 3 and 50 chars")]
        public string FullName { get; set; }

        [Display(Name = "Biography")]
        [Required(ErrorMessage = "Biography is required")]
        public string Bio { get; set; }
    }
}
