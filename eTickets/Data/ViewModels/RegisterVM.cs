using System.ComponentModel.DataAnnotations;

namespace eTickets.Data.ViewModels
{
    // This class represents the ViewModel for user registration.
    // It is used to capture and validate user input during the registration process.
    public class RegisterVM
    {
        // The user's full name.
        // The [Display] attribute specifies the label to be displayed in the UI.
        // The [Required] attribute ensures that this field is mandatory.
        // The [ErrorMessage] property provides a custom error message if the field is left empty.
        [Display(Name = "Full name")]
        [Required(ErrorMessage = "Full name is required")]
        public string FullName { get; set; }
        // The user's username.
        [Display(Name = "User name")]
        [Required(ErrorMessage = "User name is required")]
        public string UserName { get; set; }

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
        [Required]
        [DataType(DataType.Password)]
        public string Password { get; set; }

        // The confirmation of the user's password.
        // The [Display] attribute specifies the label to be displayed in the UI.
        // The [DataType] attribute specifies that this field should be treated as a password.
        // The [Compare] attribute ensures that the value matches the Password field.
        [Display(Name = "Confirm password")]
        [Required(ErrorMessage = "Confirm password is required")]
        [DataType(DataType.Password)]
        [Compare("Password", ErrorMessage = "Passwords do not match")]
        public string ConfirmPassword { get; set; }
    }
}
