package pro.ofitserov.forumtest1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import pro.ofitserov.forumtest1.entity.Role;
import pro.ofitserov.forumtest1.entity.User;
import pro.ofitserov.forumtest1.repository.RoleRepository;
import pro.ofitserov.forumtest1.repository.UserRepository;
import pro.ofitserov.forumtest1.response.ResourceNotFoundException;

import java.util.HashSet;
import java.util.Set;

@Controller
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/admin")
public class AdminController {

    private UserRepository userRepository;
    private RoleRepository roleRepository;

    @Autowired
    public AdminController(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @GetMapping("/users")
    public String users(ModelMap model) {
        model.addAttribute("title", "Users");
        model.addAttribute("users", userRepository.findAll());

        return "admin/users/list";
    }

    @GetMapping("/users/{id}/edit")
    public String edit(@PathVariable Long id, ModelMap model) {

        if (!userRepository.exists(id)) {
            throw new ResourceNotFoundException();
        }

        model.addAttribute("title", "Update user");
        model.addAttribute("user", userRepository.findOne(id));
        model.addAttribute("roles", roleRepository.findAll());

        return "admin/users/edit";
    }

    @PostMapping("/users/{id}/edit")
    public String update(@PathVariable Long id, @RequestParam(value = "roles", required = false) String roles) {

        if (!userRepository.exists(id)) {
            throw new ResourceNotFoundException();
        }

        Set<Role> newRoles = new HashSet<>();

        if ((roles != null) && (!roles.isEmpty())) {

            for (String roleAuthority : roles.split(",")) {
                newRoles.add(roleRepository.findByAuthority(roleAuthority));
            }
        }

        User user = userRepository.findOne(id);
        user.setRoles(newRoles);
        userRepository.save(user);

        return "redirect:/admin/users";
    }

    @GetMapping("/")
    public String index() {
        return "redirect:/";
    }
}
