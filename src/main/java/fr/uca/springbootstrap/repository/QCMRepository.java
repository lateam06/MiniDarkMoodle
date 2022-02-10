package fr.uca.springbootstrap.repository;

import fr.uca.springbootstrap.models.modules.questions.QCM;
import fr.uca.springbootstrap.models.modules.questions.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QCMRepository extends JpaRepository<QCM,Long> {
    Optional<QCM> findByName(String name);
}
