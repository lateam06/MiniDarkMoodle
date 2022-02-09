package fr.uca.springbootstrap.repository;

import fr.uca.springbootstrap.models.QCMResponse;
import fr.uca.springbootstrap.models.Resources;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QCMResponseRepository extends JpaRepository<QCMResponse,Long> {
}
