using System.ComponentModel.DataAnnotations;

namespace eTickets.Data.ViewModels
{
    // This class represents the ViewModel for user login.
    // It is used to capture and validate user input during the login process.
    public class LoginVM
    {
        // The user's email address.
        // The [Display] attribute specifies the label to be displayed in the UI.
        // The [Required] attribute ensures that this field is mandatory.
        // The [ErrorMessage] property provides a custom error message if the field is left empty.
        [Display(Name = "Email address")]
        [Required(ErrorMessage = "Email address is required")]
        public string EmailAddress { get; set; }

        // The user's password.
        // The [Required] attribute ensures that this field is mandatory.
        // The [DataType] attribute specifies that this field should be treated as a password,
        // which typically hides the input in the UI (e.g., replaces characters with dots or asterisks).
        [Required]
        [DataType(DataType.Password)]
        public string Password { get; set; }
    }
}
