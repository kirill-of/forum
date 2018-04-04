package pro.ofitserov.forumtest1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pro.ofitserov.forumtest1.entity.Role;
import pro.ofitserov.forumtest1.entity.Section;
import pro.ofitserov.forumtest1.entity.User;
import pro.ofitserov.forumtest1.repository.RoleRepository;
import pro.ofitserov.forumtest1.repository.SectionRepository;
import pro.ofitserov.forumtest1.repository.UserRepository;
import pro.ofitserov.forumtest1.response.ResourceNotFoundException;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Controller
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/admin")
public class AdminController {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private SectionRepository sectionRepository;

    @Autowired
    public AdminController(UserRepository userRepository, RoleRepository roleRepository, SectionRepository sectionRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.sectionRepository = sectionRepository;
    }

    @GetMapping("/users")
    public String users(ModelMap model) {
        model.addAttribute("title", "Users");
        model.addAttribute("users", userRepository.findAll());

        return "admin/users/list";
    }

    @GetMapping("/users/{id}/edit")
    public String editUser(@PathVariable Long id, ModelMap model) {

        if (!userRepository.exists(id)) {
            throw new ResourceNotFoundException();
        }

        model.addAttribute("title", "Update user");
        model.addAttribute("user", userRepository.findOne(id));
        model.addAttribute("roles", roleRepository.findAll());

        return "admin/users/edit";
    }

    @PostMapping("/users/{id}/edit")
    public String updateUser(@PathVariable Long id, @RequestParam(value = "roles", required = false) String roles) {

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

    @GetMapping("/sections")
    public String sections(ModelMap model) {
        model.addAttribute("title", "Sections");
        model.addAttribute("sections", sectionRepository.findAllByParent(null));

        return "admin/sections/list";
    }


    @GetMapping("/sections/add")
    public String addSection(ModelMap model) {
        model.addAttribute("title", "Add sections");
        model.addAttribute("sections", sectionRepository.findAll());
        model.addAttribute("section", new Section());
        return "admin/sections/add";
    }

    @PostMapping("/sections/add")
    public String updateSection(@Valid Section section, BindingResult result, @RequestParam(value = "parent_id", required = false) Long parentId, ModelMap model) {
        model.addAttribute("title", "Update section");

        if (result.hasErrors()) {
            model.addAttribute("sections", sectionRepository.findAll());
            return "admin/sections/add";
        }

        if (Objects.nonNull(parentId) && sectionRepository.exists(parentId)) {
            section.setParent(sectionRepository.findOne(parentId));
        }

        sectionRepository.save(section);

        return "redirect:/admin/sections";
    }

    @GetMapping("/sections/{id}/edit")
    public String edit(@PathVariable Long id, ModelMap model) {
        model.addAttribute("title", "Edit section");

        Section section = sectionRepository.findOne(id);

        if (Objects.isNull(section)) {
            throw new ResourceNotFoundException();
        }

        if (Objects.nonNull(section.getParent())) {
            model.addAttribute("parent_id", section.getParent());
        }

        model.addAttribute("sections", sectionRepository.findAll());
        model.addAttribute("section", section);

        return "admin/sections/add";
    }

    @GetMapping("/sections/{id}/delete")
    public String confirmRemoval(@PathVariable Long id, ModelMap model) {
        model.addAttribute("title", "Delete section");

        if (!sectionRepository.exists(id)) {
            throw new ResourceNotFoundException();
        }

        model.addAttribute("section", sectionRepository.findOne(id));

        return "admin/sections/delete";
    }

    @PostMapping("/sections/{id}/delete")
    public String remove(@PathVariable Long id) {

        if (!sectionRepository.exists(id)) {
            throw new ResourceNotFoundException();
        }

        sectionRepository.delete(id);

        return "redirect:/admin/sections";
    }

    @GetMapping("/")
    public String index() {
        return "redirect:/";
    }
}
