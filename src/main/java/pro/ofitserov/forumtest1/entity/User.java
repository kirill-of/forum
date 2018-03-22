package pro.ofitserov.forumtest1.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.Date;
import java.util.Set;

@Entity
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter
    @Setter
    private Long id;

    @NotEmpty
    @Size(min = 3, max = 30)
    @Column(unique = true)
    @Getter
    @Setter
    private String username;

    @NotEmpty
    @Size(min = 3, max = 100)
    @Getter
    @Setter
    private String password;

    @NotNull
    @Getter
    @Setter
    private Date dateOfRegistration;

    @NotNull
    @Column(unique = true)
    @Email
    @Getter
    @Setter
    private String email;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @Getter
    @Setter
    private Set<Reply> replies;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @Getter
    @Setter
    private Set<Topic> topics;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "ROLE_ASSIGNMENTS",
            joinColumns = {@JoinColumn(name = "USER_ID", referencedColumnName = "ID")},
            inverseJoinColumns = {@JoinColumn(name = "ROLE_ID", referencedColumnName = "ID")})
    @Setter
    private Set<Role> roles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
