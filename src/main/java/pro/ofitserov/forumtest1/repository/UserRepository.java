package pro.ofitserov.forumtest1.repository;

import org.springframework.data.repository.CrudRepository;
import pro.ofitserov.forumtest1.entity.User;

public interface UserRepository extends CrudRepository<User, Long> {
    User findByUsername(String username);
    User findByEmail(String email);
    User findById(Long id);
}