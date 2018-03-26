package pro.ofitserov.forumtest1.controller.form;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;
import pro.ofitserov.forumtest1.util.ForumConstants;

import javax.validation.constraints.Size;

public class ChangePasswordForm {

    @NotEmpty
    @Size(min = ForumConstants.PASSWORD_LENGTH_MIN, max = ForumConstants.PASSWORD_LENGTH_MAX)
    @Getter
    @Setter
    private String newPassword;

    @NotEmpty
    @Size(min = ForumConstants.PASSWORD_LENGTH_MIN, max = ForumConstants.PASSWORD_LENGTH_MAX)
    @Getter
    @Setter
    private String newConfirmPassword;
}
