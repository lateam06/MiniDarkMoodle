package fr.uca.springbootstrap.repository;

import fr.uca.springbootstrap.models.modules.questions.Attempt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AttemptRepository extends JpaRepository<Attempt, Long> {

    Optional<Attempt> findByQuestionIdAndUserId(Long questionId, Long userId);
}