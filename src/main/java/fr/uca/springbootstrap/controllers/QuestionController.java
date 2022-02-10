package fr.uca.springbootstrap.controllers;

import fr.uca.springbootstrap.models.modules.Module;
import fr.uca.springbootstrap.models.modules.questions.CodeRunner;
import fr.uca.springbootstrap.models.modules.questions.Questionnary;
import fr.uca.springbootstrap.models.users.User;
import fr.uca.springbootstrap.payload.request.CodeRequest;
import fr.uca.springbootstrap.payload.response.MessageResponse;
import fr.uca.springbootstrap.repository.*;
import fr.uca.springbootstrap.security.services.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

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
    CodeRunnerRepository codeRunnerRepository;


    @Autowired
    QuestionnaryRepository questionnaryRepository;

    @PostMapping("/{moduleId}/resources/{resourcesId}/{questionId}/sendCode")
    public ResponseEntity<?> submitCode(Principal principal, @PathVariable long moduleId, @PathVariable long resourcesId, @PathVariable long studentId, @Valid @RequestBody CodeRequest body) {
        Optional<CodeRunner> orunner = codeRunnerRepository.findById(resourcesId);
        Optional<Module> omodule = moduleRepository.findById(moduleId);
        Optional<Questionnary> oquestionnary = questionnaryRepository.findById(resourcesId);
        User actor = userRepository.findByUsername(principal.getName()).get();

        if (orunner.isEmpty() || omodule.isEmpty() || oquestionnary.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Module mod = omodule.get();
        Questionnary quest = oquestionnary.get();
        CodeRunner runner = orunner.get();
        if (!mod.getParticipants().contains(actor)) {

            return ResponseEntity.status(403).body(new MessageResponse("User not registered to this module"));
        }

        if (!mod.getResources().contains(quest) || !quest.getQuestionSet().contains(runner)) {
            return ResponseEntity.notFound().build();
        }

        runner.setStudentResponse(body.getCode());

        codeRunnerRepository.save(runner);
        return ResponseEntity.ok().body(new MessageResponse("Code Submitted !"));

    }


}
