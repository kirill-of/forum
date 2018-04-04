package pro.ofitserov.forumtest1.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;
import pro.ofitserov.forumtest1.util.ForumConstants;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class Section {
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
    @Size(min = ForumConstants.DESCRIPTION_LENGTH_MIN, max = ForumConstants.DESCRIPTION_LENGTH_MAX)
    @Getter
    @Setter
    private String description;

    @OneToMany(mappedBy = "section", cascade = CascadeType.ALL)
    @Getter
    @Setter
    private Set<Topic> topics;

    @ManyToOne
    @JoinColumn(name = "PARENT_ID")
    @Getter
    @Setter
    private Section parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    @Getter
    @Setter
    private Set<Section> subsections = new HashSet<>();

    public boolean getIsParent() {
        return (Objects.nonNull(subsections) && subsections.size() > 0);
    }
}
