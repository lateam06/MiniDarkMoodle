package fr.uca.springbootstrap.controllers;

import fr.uca.springbootstrap.models.modules.Module;
import fr.uca.springbootstrap.models.modules.questions.QCM;
import fr.uca.springbootstrap.models.modules.questions.Question;
import fr.uca.springbootstrap.models.modules.questions.Questionnary;
import fr.uca.springbootstrap.models.users.User;
import fr.uca.springbootstrap.payload.request.CreateNewQCMRequest;
import fr.uca.springbootstrap.payload.response.MessageResponse;
import fr.uca.springbootstrap.repository.*;
import fr.uca.springbootstrap.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.persistence.DiscriminatorValue;
import javax.validation.Valid;
import java.security.Principal;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/module")
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

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    QCMRepository qcmRepository;

    @Autowired
    OpenQuestionRepository openQuestionRepository;

    private ResponseEntity<?> checkModuleQuestionnaryUser(Principal principal, long moduleId, long resourcesId) {
        Optional<Module> optionalModule = moduleRepository.findById(moduleId);
        Optional<Questionnary> optionalQuestionnary = questionnaryRepository.findById(resourcesId);

        if (!optionalModule.isPresent()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: no such module !"));
        }
        if (!optionalQuestionnary.isPresent()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: no such a questionnary !"));
        }

        Module module = optionalModule.get();
        Questionnary questionnary = optionalQuestionnary.get();

        if (!module.getResources().contains(questionnary)) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: The given questionnary is not found on the given module."));
        }

        Optional<User> optionalUser = userRepository.findByUsername(principal.getName());
        if (optionalUser.isPresent()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: You're not on the user database."));
        }
        User user = optionalUser.get();

        if (!module.getParticipants().contains(user)) {
            return ResponseEntity.status(403).body(new MessageResponse("Error: You're not an user registered on this module."));
        }

        return null;
    }

    @PostMapping("/{moduleId}/resources/{resourcesId}/newQcm")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> createNewQCM(Principal principal, @PathVariable long moduleId, @PathVariable long resourcesId, @Valid @RequestBody CreateNewQCMRequest cnqRequest) {
        var responseCheck = checkModuleQuestionnaryUser(principal, moduleId, resourcesId);
        if (responseCheck != null)
            return responseCheck;

        QCM qcm = new QCM(cnqRequest.getName(), cnqRequest.getDescription(), cnqRequest.getResponse());
        qcmRepository.save(qcm);
        return ResponseEntity.ok(new MessageResponse("QCM added succesfully!"));
    }

    @GetMapping("/{moduleId}/resources/{resourcesId}/{questionId}")
    public ResponseEntity<?> getQuestion(Principal principal, @PathVariable long moduleId, @PathVariable long resourcesId, @PathVariable long questionId) {
        var responseCheck = checkModuleQuestionnaryUser(principal, moduleId, resourcesId);
        if (responseCheck != null)
            return responseCheck;

        Questionnary questionnary = questionnaryRepository.findById(resourcesId).get();
        Optional<Question> optionalQuestion = questionRepository.findById(questionId);
        if (!optionalQuestion.isPresent()) {
            return ResponseEntity.badRequest().body("Error: the question doesn't exists.");
        }
        Question question = optionalQuestion.get();

        if (question.getQuestionnary().getId() != questionnary.getId()) {
            return ResponseEntity.badRequest().body("Error: this question isn't in this module.");
        }

        String discriminator = question.getClass().getAnnotation(DiscriminatorValue.class).value();

        switch (discriminator) {
            case "qcms":
                return ResponseEntity.ok(qcmRepository.findById(question.getId()));
            case "open_questions":
                return ResponseEntity.ok(openQuestionRepository.findById(question.getId()));
            default:
                return ResponseEntity.ok(question);
        }
    }


}
