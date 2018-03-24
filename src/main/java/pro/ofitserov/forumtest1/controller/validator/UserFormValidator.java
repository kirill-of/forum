package pro.ofitserov.forumtest1.controller.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import pro.ofitserov.forumtest1.controller.form.UserRegistrationForm;
import pro.ofitserov.forumtest1.entity.User;
import pro.ofitserov.forumtest1.repository.UserRepository;

import java.util.Objects;

@Component
public class UserFormValidator implements Validator {

    private UserRepository userRepository;

    public UserFormValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return UserRegistrationForm.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        UserRegistrationForm userRegistrationForm = (UserRegistrationForm) o;

        if (!userRegistrationForm.getPassword().equals(userRegistrationForm.getConfirmPassword())) {
            errors.rejectValue("confirmPassword", "registration.error.passwordsNotMatch");
        }

        User savedUserByName = userRepository.findByUsername(userRegistrationForm.getUsername());
        User savedUserByEmail = userRepository.findByEmail(userRegistrationForm.getEmail());

        if (Objects.nonNull(savedUserByName)) {
            errors.rejectValue("username", "registration.error.username.nonUnique");
        }

        if (Objects.nonNull(savedUserByEmail)) {
            errors.rejectValue("email", "registration.error.email.nonUnique");
        }
    }
}