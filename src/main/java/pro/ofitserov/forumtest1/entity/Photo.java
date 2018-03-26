package pro.ofitserov.forumtest1.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Photo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter
    @Setter
    private Long id;

    @Lob
    @Getter
    @Setter
    private byte[] photo;

    @OneToOne
    @JoinColumn(name = "USER_ID")
    @Getter
    @Setter
    private User user;

    public Photo(byte[] photo, User user) {
        this.photo = photo;
        this.user = user;
    }
}
