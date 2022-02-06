package fr.uca.springbootstrap.repository;

import fr.uca.springbootstrap.models.Ressources;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RessourcesRepository extends JpaRepository<Ressources,Long> {
    @Override
    Optional<Ressources> findById(Long aLong);

    Optional<Ressources> findByName(String name);


}
