package pro.ofitserov.forumtest1.repository;

import org.springframework.data.repository.CrudRepository;
import pro.ofitserov.forumtest1.entity.Reply;
import pro.ofitserov.forumtest1.entity.Topic;

import java.util.List;

public interface ReplyRepository extends CrudRepository<Reply, Long> {
    List<Reply> findByTopic(Topic topic);
}
