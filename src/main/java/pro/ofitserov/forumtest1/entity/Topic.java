package pro.ofitserov.forumtest1.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;
import pro.ofitserov.forumtest1.util.ForumConstants;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.Objects;
import java.util.Set;

@Entity
public class Topic {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter
    @Setter
    private Long id;

    @NotEmpty
    @Size(min = ForumConstants.TITLE_LENGTH_MIN, max = ForumConstants.TITLE_LENGTH_MAX)
    @Getter
    @Setter
    private String title;

    @NotEmpty
    @Size(min = ForumConstants.TEXT_LENGTH_MIN, max = ForumConstants.TEXT_LENGTH_MAX)
    @Getter
    @Setter
    private String text;

    @Temporal(TemporalType.TIMESTAMP)
    @Getter
    @Setter
    private Date dateOfPublication;

    @Temporal(TemporalType.TIMESTAMP)
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

    @ManyToOne
    @JoinColumn(name = "SECTION_ID")
    @Getter
    @Setter
    private Section section;

    @OneToMany(mappedBy = "topic", cascade = CascadeType.ALL)
    @Getter
    @Setter
    private Set<Reply> replies;

    public boolean getIsChanged() {
        return Objects.nonNull(dateOfChange);
    }
}
