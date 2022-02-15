package fr.uca.springbootstrap.repository;

import fr.uca.springbootstrap.models.modules.questions.QCMAttempt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QCMAttemptRepository extends JpaRepository<QCMAttempt, Long> {
    Optional<QCMAttempt> findByUserIdAndQuestionId(Long userId, Long questionId);
}