package fr.uca.springbootstrap.repository;

import fr.uca.springbootstrap.models.modules.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResourcesRepository extends JpaRepository<Resource,Long> {
    @Override
    Optional<Resource> findById(Long aLong);

    Optional<Resource> findByName(String name);


}
