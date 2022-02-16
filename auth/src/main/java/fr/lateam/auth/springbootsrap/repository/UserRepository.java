package fr.lateam.auth.springbootsrap.repository;

import fr.lateam.auth.springbootsrap.models.users.User;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
        Optional<User> findByUsername(String username);
        //Optional<User> findByEmail(String email);
        Optional<User> findByUsernameAndEmail(String username, String email);

        Boolean existsByUsername(String username);

        Boolean existsByEmail(String email);

}
