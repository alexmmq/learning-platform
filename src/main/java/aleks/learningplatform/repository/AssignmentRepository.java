package aleks.learningplatform.repository;

import aleks.learningplatform.domain.content.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssignmentRepository extends JpaRepository<Assignment, Long> {}
