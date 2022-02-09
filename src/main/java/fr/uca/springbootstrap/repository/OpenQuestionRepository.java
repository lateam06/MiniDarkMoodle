package fr.uca.springbootstrap.repository;

import fr.uca.springbootstrap.models.OpenQuestion;
import fr.uca.springbootstrap.models.Resources;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OpenQuestionRepository extends JpaRepository<OpenQuestion,Long> {
}
