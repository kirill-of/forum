package pro.ofitserov.forumtest1.repository;

import org.springframework.data.repository.CrudRepository;
import pro.ofitserov.forumtest1.entity.Role;

public interface RoleRepository extends CrudRepository<Role, Long> {
    Role findByAuthority(String authority);
}
