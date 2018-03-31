package pro.ofitserov.forumtest1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pro.ofitserov.forumtest1.entity.Role;
import pro.ofitserov.forumtest1.entity.User;
import pro.ofitserov.forumtest1.repository.RoleRepository;
import pro.ofitserov.forumtest1.repository.UserRepository;

import java.util.Collections;
import java.util.Objects;

@Service
public class UserService implements UserDetailsService {

    private static final long ID_ROLE_FOR_NEW_USER = 1L;

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (Objects.isNull(user)) {
            throw new UsernameNotFoundException("Can't find user with username " + username);
        }
        return user;
    }

    public void signupUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role userRole = roleRepository.findOne(ID_ROLE_FOR_NEW_USER);
        user.setRoles(Collections.singleton(userRole));
        userRepository.save(user);
    }

    public User getCurrentUser() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().
                getAuthentication().getPrincipal();
        User user = null;
        if (userDetails instanceof User) {
            user = (User) userDetails;
        }
        return user;
    }

    public boolean isCurrentUserId(Long id) {
        User user = getCurrentUser();
        return (Objects.nonNull(user) && (user.getId().equals(id)));
    }

    public Long getCurrentUserId() {
        Object object = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (Objects.nonNull(object)) {
            try {
                UserDetails userDetails = (UserDetails) object;
                User user;
                if (userDetails instanceof User) {
                    user = (User) userDetails;
                    return user.getId();
                } else {
                    return null;
                }
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }
}
