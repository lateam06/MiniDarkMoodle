package fr.uca.springbootstrap.repository;

import fr.uca.springbootstrap.models.modules.questions.OpenAttempt;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OpenAttemptRepository extends JpaRepository<OpenAttempt, Long> {

}
