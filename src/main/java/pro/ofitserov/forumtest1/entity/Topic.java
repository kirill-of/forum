package pro.ofitserov.forumtest1.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;
import pro.ofitserov.forumtest1.util.ForumConstants;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.Set;

@Entity
public class Topic {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter
    @Setter
    private Long id;

    @NotEmpty
    @Size(min = ForumConstants.MIN_LENGTH_TITLE, max = ForumConstants.MAX_LENGTH_TITLE)
    @Getter
    @Setter
    private String title;

    @NotEmpty
    @Size(min = ForumConstants.MIN_LENGTH_TEXT, max = ForumConstants.MAX_LENGTH_TEXT)
    @Getter
    @Setter
    private String text;

    @Getter
    @Setter
    private Date dateOfPublication;

    @Getter
    @Setter
    private Date dateOfChange;

    @ManyToOne
    @JoinColumn(name = "CHANGED_USER_ID")
    @Getter
    @Setter
    private User changedUser;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    @Getter
    @Setter
    private User user;

    @OneToMany(mappedBy = "topic", cascade = CascadeType.ALL)
    @Getter
    @Setter
    private Set<Reply> replies;
}
