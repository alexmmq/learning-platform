package aleks.learningplatform.repository;

import aleks.learningplatform.domain.content.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LessonRepository extends JpaRepository<Lesson, Long> {}
