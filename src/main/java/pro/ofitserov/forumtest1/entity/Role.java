package pro.ofitserov.forumtest1.entity;


import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

@Entity
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    private String name;

    @Column(unique = true)
    @Getter
    @Setter
    private String authority;

    @Override
    public String toString() {
        return name;
    }
}

