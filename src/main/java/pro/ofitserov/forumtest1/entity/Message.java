package pro.ofitserov.forumtest1.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    private String text;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    /*
    private Date dateOfPublication;
    private topic_id;
    private Date dateOfChange;
    private change_user_id;
    */
}
