package fr.uca.springbootstrap.repository;

import fr.uca.springbootstrap.models.modules.courses.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
}
