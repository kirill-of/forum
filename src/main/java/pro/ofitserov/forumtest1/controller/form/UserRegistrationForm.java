package pro.ofitserov.forumtest1.controller.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

public class UserRegistrationForm {

    @NotNull
    @Getter
    @Setter
    private String username;

    @NotNull
    @Getter
    @Setter
    private String password;

    @NotNull
    @Getter
    @Setter
    private String confirmPassword;
}