package fr.uca.springbootstrap.repository;

import fr.uca.springbootstrap.models.modules.questions.OpenQuestion;
import fr.uca.springbootstrap.models.modules.questions.QCM;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OpenQuestionRepository extends JpaRepository<OpenQuestion,Long> {
    Optional<OpenQuestion> findByName(String name);
}
