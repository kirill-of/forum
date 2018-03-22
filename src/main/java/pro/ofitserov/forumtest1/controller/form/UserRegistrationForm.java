package pro.ofitserov.forumtest1.controller.form;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Email;

public class UserRegistrationForm {

    @Getter
    @Setter
    private String username;

    @Email
    @Getter
    @Setter
    private String email;

    @Getter
    @Setter
    private String password;

    @Getter
    @Setter
    private String confirmPassword;
}