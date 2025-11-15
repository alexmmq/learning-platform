package aleks.learningplatform.repository;

import aleks.learningplatform.domain.course.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {}
