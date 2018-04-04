package pro.ofitserov.forumtest1.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import pro.ofitserov.forumtest1.entity.Section;

import java.util.Set;

public interface SectionRepository extends PagingAndSortingRepository<Section, Long> {
    Set<Section> findAllByParent(Section parent);
}
