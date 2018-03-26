package pro.ofitserov.forumtest1.repository;

import org.springframework.data.repository.CrudRepository;
import pro.ofitserov.forumtest1.entity.Photo;

public interface PhotoRepository  extends CrudRepository<Photo, Long> {
    Photo findByUserId(Long id);
}
