package aleks.learningplatform.repository;

import aleks.learningplatform.domain.course.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByCategory_Name(String categoryName);
}
