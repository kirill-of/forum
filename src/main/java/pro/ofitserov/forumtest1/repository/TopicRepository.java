package pro.ofitserov.forumtest1.repository;

import org.springframework.data.repository.CrudRepository;
import pro.ofitserov.forumtest1.entity.Topic;

public interface TopicRepository extends CrudRepository<Topic, Long> {

}
