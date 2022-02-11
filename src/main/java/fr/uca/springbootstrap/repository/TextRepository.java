package fr.uca.springbootstrap.repository;

import fr.uca.springbootstrap.models.modules.courses.Text;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TextRepository extends JpaRepository<Text, Long> {

    Optional<Text> findByParagraph(String paragraph);
}
