package fr.uca.springbootstrap.repository;

import java.util.Optional;

import fr.uca.springbootstrap.models.users.UserApi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserApiRepository extends JpaRepository<UserApi, Long> {
  Optional<UserApi> findByUsername(String username);
  Boolean existsByUsername(String username);
}
