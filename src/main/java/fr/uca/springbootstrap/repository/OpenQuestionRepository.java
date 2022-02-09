package fr.uca.springbootstrap.repository;

import fr.uca.springbootstrap.models.modules.questions.OpenQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OpenQuestionRepository extends JpaRepository<OpenQuestion,Long> {
}
