package pro.ofitserov.forumtest1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pro.ofitserov.forumtest1.repository.UserRepository;
import pro.ofitserov.forumtest1.service.UserService;

@Controller
@PreAuthorize("hasRole('USER')")
public class ProfileController {

    @Autowired
    private UserService userService;

    private UserRepository userRepository;

    public ProfileController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/profile")
    public String profile(Model model) {
        model.addAttribute("title", "Profile");
        model.addAttribute("user", userRepository.findById(userService.getCurrentUser().getId()));
        return "profile/view";
    }
}
