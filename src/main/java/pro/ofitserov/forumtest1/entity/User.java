package pro.ofitserov.forumtest1.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import pro.ofitserov.forumtest1.util.ForumConstants;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.Date;
import java.util.Objects;
import java.util.Set;

@Entity
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter
    @Setter
    private Long id;

    @NotEmpty
    @Size(min = ForumConstants.USERNAME_LENGTH_MIN, max = ForumConstants.USERNAME_LENGTH_MAX)
    @Column(unique = true)
    @Getter
    @Setter
    private String username;

    @NotEmpty
    @Column(unique = true)
    @Email
    @Getter
    @Setter
    private String email;

    @NotEmpty
    @Getter
    @Setter
    private String password;

    @Temporal(TemporalType.TIMESTAMP)
    @Getter
    @Setter
    private Date dateOfRegistration;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @Getter
    @Setter
    private Set<Reply> replies;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @Getter
    @Setter
    private Set<Topic> topics;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @Getter
    @Setter
    private Photo photo;

    public boolean getPhotoExist() {
        return Objects.nonNull(photo);
    }

    public int getCountPosts() {
        return (topics.size() + replies.size());
    }

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "ROLE_ASSIGNMENTS",
            joinColumns = {@JoinColumn(name = "USER_ID", referencedColumnName = "ID")},
            inverseJoinColumns = {@JoinColumn(name = "ROLE_ID", referencedColumnName = "ID")})
    @Setter
    private Set<Role> roles;

    public boolean getHasRoleById(Long roleId) {
        for (Role role : roles) {
            if (role.getId().equals(roleId)) {
                return true;
            }
        }
        return false;
    }

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
