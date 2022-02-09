package fr.uca.springbootstrap.repository;

import fr.uca.springbootstrap.models.modules.questions.Result;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ResultRepository extends JpaRepository<Result, Long> {

    Optional<Result> findByQuestionIdAndUserId(Long questionId, Long userId);

}
