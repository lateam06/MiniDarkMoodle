package fr.uca.springbootstrap.repository;

import fr.uca.springbootstrap.models.modules.questions.QCMResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QCMResponseRepository extends JpaRepository<QCMResponse,Long> {

    Optional<QCMResponse> findByDescription(String description);

}
