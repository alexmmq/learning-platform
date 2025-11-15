package aleks.learningplatform.repository;

import aleks.learningplatform.domain.content.Module;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ModuleRepository extends JpaRepository<Module, Long> {}
