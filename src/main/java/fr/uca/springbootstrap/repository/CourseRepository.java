package fr.uca.springbootstrap.repository;

import fr.uca.springbootstrap.models.modules.Module;
import fr.uca.springbootstrap.models.modules.courses.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    Optional<Course> findById(Long id);
    Optional<Course> findByName(String name);

}
