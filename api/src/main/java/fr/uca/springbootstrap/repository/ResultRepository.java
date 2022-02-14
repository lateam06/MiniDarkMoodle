package fr.uca.springbootstrap.repository;

import fr.uca.springbootstrap.models.modules.questions.Result;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResultRepository extends JpaRepository<Result, Long> {
}
