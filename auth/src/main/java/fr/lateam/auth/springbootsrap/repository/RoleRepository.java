package fr.lateam.auth.springbootsrap.repository;

import fr.lateam.auth.springbootsrap.models.users.ERole;
import fr.lateam.auth.springbootsrap.models.users.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
  Optional<Role> findByName(ERole name);
}