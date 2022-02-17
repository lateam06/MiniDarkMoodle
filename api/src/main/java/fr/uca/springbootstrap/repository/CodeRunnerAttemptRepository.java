package fr.uca.springbootstrap.repository;

import fr.uca.springbootstrap.models.modules.questions.CodeRunnerAttempt;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CodeRunnerAttemptRepository extends JpaRepository<CodeRunnerAttempt, Long> {

    Optional<CodeRunnerAttempt> findByQuestionIdAndUserId(Long questionId, Long userId);
}
