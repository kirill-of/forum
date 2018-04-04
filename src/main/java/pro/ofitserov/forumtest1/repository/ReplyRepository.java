package pro.ofitserov.forumtest1.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import pro.ofitserov.forumtest1.entity.Reply;
import pro.ofitserov.forumtest1.entity.Topic;


public interface ReplyRepository extends PagingAndSortingRepository<Reply, Long> {
    Page<Reply> findByTopic(Topic topic, Pageable pageable);

    Page<Reply> findByTextContaining(String searchWord, Pageable pageable);
}
