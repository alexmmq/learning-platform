package aleks.learningplatform.repository;

import aleks.learningplatform.domain.course.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {}
