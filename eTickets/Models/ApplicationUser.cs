using Microsoft.AspNetCore.Identity;
using System.ComponentModel.DataAnnotations;

namespace eTickets.Models
{
    // This class represents the application's user entity.
    // It extends the IdentityUser class to include additional properties specific to the application.
    public class ApplicationUser : IdentityUser
    {
        // The full name of the user.
        // The [Display] attribute specifies the label to be displayed in the UI.
        [Display(Name = "Full name")]
        public string FullName { get; set; }
        
    }
}
