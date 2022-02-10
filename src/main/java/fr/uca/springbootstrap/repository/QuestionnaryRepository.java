package fr.uca.springbootstrap.repository;

import fr.uca.springbootstrap.models.modules.questions.Question;
import fr.uca.springbootstrap.models.modules.questions.Questionnary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QuestionnaryRepository extends JpaRepository<Questionnary,Long> {
    @Override
    Optional<Questionnary> findById(Long aLong);

    Optional<Questionnary> findByName(String name);
}
