package fr.lateam.auth.springbootsrap.repository;

import fr.lateam.auth.springbootsrap.models.AuthUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface AuthUserRespository extends JpaRepository<AuthUser, Long> {
        Optional<AuthUser> findByUsername(String username);

        Boolean existsByUsername(String username);

        Boolean existsByEmail(String email);

}
