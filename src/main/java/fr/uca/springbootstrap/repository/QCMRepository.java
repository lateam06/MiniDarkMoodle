package fr.uca.springbootstrap.repository;

import fr.uca.springbootstrap.models.modules.questions.QCM;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QCMRepository extends JpaRepository<QCM,Long> {
}
