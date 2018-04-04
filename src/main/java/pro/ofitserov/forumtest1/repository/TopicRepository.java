package pro.ofitserov.forumtest1.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import pro.ofitserov.forumtest1.entity.Section;
import pro.ofitserov.forumtest1.entity.Topic;

public interface TopicRepository extends PagingAndSortingRepository<Topic, Long> {
    Page<Topic> findBySection(Section section, Pageable pageable);

    Page<Topic> findByTitleContainingOrTextContaining(String searchWordInTitle, String searchWordInText, Pageable pageable);
}
