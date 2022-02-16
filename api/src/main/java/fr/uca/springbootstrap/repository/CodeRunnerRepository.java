package fr.uca.springbootstrap.repository;

import fr.uca.springbootstrap.models.modules.questions.CodeRunner;
import fr.uca.springbootstrap.models.modules.questions.QCM;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CodeRunnerRepository extends JpaRepository<CodeRunner, Long> {

    @Override
    Optional<CodeRunner> findById(Long aLong);

    Optional<CodeRunner> findByName(String name);
}
