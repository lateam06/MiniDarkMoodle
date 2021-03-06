package fr.uca.springbootstrap.repository;

import fr.uca.springbootstrap.models.modules.questions.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QuestionRepository extends JpaRepository<Question,Long> {
    @Override
    Optional<Question> findById(Long aLong);

    Optional<Question> findByName(String name);
}
