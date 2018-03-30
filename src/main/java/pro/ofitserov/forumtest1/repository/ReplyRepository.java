package pro.ofitserov.forumtest1.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import pro.ofitserov.forumtest1.entity.Reply;
import pro.ofitserov.forumtest1.entity.Topic;

import java.util.List;

public interface ReplyRepository extends PagingAndSortingRepository<Reply, Long> {
    List<Reply> findByTopic(Topic topic);

    Page<Reply> findByTopic(Topic topic, Pageable pageable);
}
