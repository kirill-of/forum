package pro.ofitserov.forumtest1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import pro.ofitserov.forumtest1.controller.form.UserRegistrationForm;
import pro.ofitserov.forumtest1.controller.validator.UserFormValidator;
import pro.ofitserov.forumtest1.entity.User;
import pro.ofitserov.forumtest1.repository.UserRepository;
import pro.ofitserov.forumtest1.service.UserService;

import javax.validation.Valid;
import java.util.Date;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    private UserFormValidator userFormValidator;

    private UserRepository userRepository;

    public AuthController(UserFormValidator userFormValidator) {
        this.userFormValidator = userFormValidator;
    }

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(userFormValidator);
    }

    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("title", "Registration");
        model.addAttribute("userRegistrationForm", new UserRegistrationForm());
        return "auth/registration";
    }

    @PostMapping("/registration")
    public String registration(
            @Valid UserRegistrationForm userRegistrationForm,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "auth/registration";
        }

        User user = new User();
        user.setUsername(userRegistrationForm.getUsername());
        user.setEmail(userRegistrationForm.getEmail());
        user.setPassword(userRegistrationForm.getPassword());
        user.setDateOfRegistration(new Date());

        userService.signupUser(user);

        return "redirect:/";
    }

    @GetMapping("/login")
    public String login(Model model, String error, String logout) {

        model.addAttribute("title", "Sign In");

        if (error != null)
            model.addAttribute("error", "Your username and password is invalid.");

        if (logout != null)
            model.addAttribute("message", "You have been logged out successfully.");

        return "auth/login";
    }
}
