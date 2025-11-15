package aleks.learningplatform.repository;

import aleks.learningplatform.domain.course.CourseReview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseReviewRepository extends JpaRepository<CourseReview, Long> {}
