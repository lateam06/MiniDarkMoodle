package fr.uca.springbootstrap.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import fr.uca.springbootstrap.models.modules.Module;
import fr.uca.springbootstrap.models.modules.Resource;
import fr.uca.springbootstrap.models.modules.questions.Question;
import fr.uca.springbootstrap.models.modules.questions.Questionnary;
import fr.uca.springbootstrap.models.users.User;
import fr.uca.springbootstrap.payload.response.MessageResponse;
import fr.uca.springbootstrap.repository.*;
import fr.uca.springbootstrap.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/modules/")
public class QuestionController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    ModuleRepository moduleRepository;

    @Autowired
    ResourcesRepository resourcesRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    QuestionnaryRepository questionnaryRepository;


    @GetMapping("/{moduleId}/resources/{questionnaryId}/result/{userid}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> getStudentsResponses(@PathVariable long moduleId, @PathVariable long questionnaryId, @PathVariable long userId) throws JsonProcessingException {
        Optional<Module> omodule = moduleRepository.findById(moduleId);
        Optional<Questionnary> oquestionnary = questionnaryRepository.findById(questionnaryId);
        Optional<User> ouser = userRepository.findById(userId);

        if (!omodule.isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: No such Module"));
        }

        if (!oquestionnary.isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: No such questionnary in the module"));
        }

        if (!ouser.isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: No such user"));
        }

        Questionnary questionnary = oquestionnary.get();
        int grade = questionnary.getStudentGrade(userId);
        return ResponseEntity.ok(new MessageResponse(String.format("student grade is %d", grade)));
    }
}
