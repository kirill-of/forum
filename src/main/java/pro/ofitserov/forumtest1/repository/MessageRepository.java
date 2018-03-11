package pro.ofitserov.forumtest1.repository;

import org.springframework.data.repository.CrudRepository;
import pro.ofitserov.forumtest1.entity.Message;

public interface MessageRepository extends CrudRepository<Message, Long> {

}
