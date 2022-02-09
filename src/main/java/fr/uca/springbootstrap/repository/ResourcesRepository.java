package fr.uca.springbootstrap.repository;

import fr.uca.springbootstrap.models.modules.Resources;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResourcesRepository extends JpaRepository<Resources,Long> {
    @Override
    Optional<Resources> findById(Long aLong);

    Optional<Resources> findByName(String name);


}
