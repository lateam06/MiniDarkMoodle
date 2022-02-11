package fr.uca.springbootstrap.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import fr.uca.springbootstrap.models.modules.Module;
import fr.uca.springbootstrap.models.modules.questions.*;
import fr.uca.springbootstrap.models.users.User;
import fr.uca.springbootstrap.payload.response.MessageResponse;

import fr.uca.springbootstrap.payload.request.CodeRequest;

import fr.uca.springbootstrap.payload.request.CreateNewOpenRequest;
import fr.uca.springbootstrap.payload.request.CreateNewQCMRequest;
import fr.uca.springbootstrap.repository.*;

import fr.uca.springbootstrap.security.services.jwt.JwtUtils;
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

    @Autowired
    CodeRunnerRepository codeRunnerRepository;

    private ResponseEntity<?> checkModuleQuestionnaryUser(Principal principal, long moduleId, long resourcesId) {
        Optional<Module> optionalModule = moduleRepository.findById(moduleId);
        Optional<Questionnary> optionalQuestionnary = questionnaryRepository.findById(resourcesId);

        if (optionalModule.isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: no such module !"));
        }
        if (optionalQuestionnary.isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: no such a questionnary !"));
        }

        Module module = optionalModule.get();
        Questionnary questionnary = optionalQuestionnary.get();

        if (!module.getResources().contains(questionnary)) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: The given questionnary is not found on the given module."));
        }

        Optional<User> optionalUser = userRepository.findByUsername(principal.getName());
        if (optionalUser.isEmpty()) {
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

        Questionnary questionnary = questionnaryRepository.findById(resourcesId).get();
        QCM qcm = qcmRepository.findByName(cnqRequest.getName()).orElse(new QCM(cnqRequest.getName(), cnqRequest.getDescription(), cnqRequest.getResponse()));

        if (questionnary.getQuestionSet().contains(qcm)) {
            return ResponseEntity.badRequest().body("Error: this qcm is already on the questionnaire");
        }

        questionnary.getQuestionSet().add(qcm);
        qcmRepository.save(qcm);
        questionnaryRepository.save(questionnary);
        return ResponseEntity.ok(new MessageResponse("QCM added succesfully!"));
    }

    @PostMapping("/{moduleId}/resources/{resourcesId}/newOpen")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> createNewOpen(Principal principal, @PathVariable long moduleId, @PathVariable long resourcesId, @Valid @RequestBody CreateNewOpenRequest cnoRequest) {
        var responseCheck = checkModuleQuestionnaryUser(principal, moduleId, resourcesId);
        if (responseCheck != null)
            return responseCheck;

        Questionnary questionnary = questionnaryRepository.findById(resourcesId).get();
        Optional<OpenQuestion> optionalOpenQuestion = openQuestionRepository.findByName(cnoRequest.getName());
        if (optionalOpenQuestion.isEmpty() && resourcesRepository.findByName(cnoRequest.getName()).isPresent()) {
            return ResponseEntity.badRequest().body("Error: a question has already on this name");
        }

        OpenQuestion open = optionalOpenQuestion.orElse(new OpenQuestion(cnoRequest.getName(), cnoRequest.getDescription(), cnoRequest.getResponse()));

        if (questionnary.getQuestionSet().contains(open)) {
            return ResponseEntity.badRequest().body("Error: this open question is already on the questionnaire");
        }

        questionnary.getQuestionSet().add(open);
        openQuestionRepository.save(open);
        questionnaryRepository.save(questionnary);
        return ResponseEntity.ok(new MessageResponse("OpenQuestion added succesfully!"));
    }

    @GetMapping("/{moduleId}/resources/{resourcesId}/{questionId}")
    public ResponseEntity<?> getQuestion(Principal principal, @PathVariable long moduleId, @PathVariable long resourcesId, @PathVariable long questionId) {
        var responseCheck = checkModuleQuestionnaryUser(principal, moduleId, resourcesId);
        if (responseCheck != null)
            return responseCheck;

        Questionnary questionnary = questionnaryRepository.findById(resourcesId).get();
        Optional<Question> optionalQuestion = questionRepository.findById(questionId);
        if (optionalQuestion.isEmpty()) {
            return ResponseEntity.badRequest().body("Error: the question doesn't exists.");
        }
        Question question = optionalQuestion.get();

        if (!questionnary.getQuestionSet().contains(question)) {
            return ResponseEntity.badRequest().body("Error: this question isn't in this module.");
        }

        String discriminator = question.getClass().getAnnotation(DiscriminatorValue.class).value();

        switch (discriminator) {
            case "qcms":
                QCM qcm = qcmRepository.findById(question.getId()).get();
                var qcmr = new CreateNewQCMRequest(qcm.getName(), qcm.getDescription(), qcm.getResponse());
                return ResponseEntity.ok(qcmr);
            case "open_questions":
                OpenQuestion op = openQuestionRepository.findById(question.getId()).get();
                var opr = new CreateNewOpenRequest(op.getName(), op.getDescription(), op.getResponse());
                return ResponseEntity.ok(opr);
            default:
                return ResponseEntity.ok(question);
        }
    }

    @DeleteMapping("/{moduleId}/resources/{resourcesId}/{questionId}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> deleteQuestion(Principal principal, @PathVariable long moduleId, @PathVariable long resourcesId, @PathVariable long questionId) {
        var responseCheck = checkModuleQuestionnaryUser(principal, moduleId, resourcesId);
        if (responseCheck != null)
            return responseCheck;

        Questionnary questionnary = questionnaryRepository.findById(resourcesId).get();
        Optional<Question> optionalQuestion = questionRepository.findById(questionId);
        if (optionalQuestion.isEmpty()) {
            return ResponseEntity.badRequest().body("Error: the question doesn't exists.");
        }
        Question question = optionalQuestion.get();

        if (!questionnary.getQuestionSet().contains(question)) {
            return ResponseEntity.badRequest().body("Error: this question isn't in this module.");
        }

        questionRepository.deleteById(question.getId());
        return ResponseEntity.ok("Question deleted.");
    }

    @PostMapping("/{moduleId}/resources/{resourcesId}/{questionId}/sendCode")
    public ResponseEntity<?> submitCode(Principal principal, @PathVariable long moduleId, @PathVariable long resourcesId, @PathVariable long questionId, @Valid @RequestBody CodeRequest body) {
        Optional<CodeRunner> orunner = codeRunnerRepository.findById(questionId);
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

    @GetMapping("/{moduleId}/resources/{questionnaryId}/result/{userid}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> getStudentsResponses(@PathVariable long moduleId, @PathVariable long questionnaryId, @PathVariable long userid) {
        Optional<Module> omodule = moduleRepository.findById(moduleId);
        Optional<Questionnary> oquestionnary = questionnaryRepository.findById(questionnaryId);
        Optional<User> ouser = userRepository.findById(userid);

        if (omodule.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: No such Module"));
        }

        if (oquestionnary.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: No such questionnary in the module"));
        }

        if (ouser.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: No such user"));
        }

        Questionnary questionnary = oquestionnary.get();
//        int grade = questionnary.getStudentGrade(userid);
//        return ResponseEntity.ok(new MessageResponse(String.format("student grade is %d", grade)));
        return ResponseEntity.ok(new MessageResponse("student grade is ???"));
    }

//    public void validateQuestionnary(Long studentId) {
//        int rate = 0;
//        for (Question question : questionSet) {
//            for (Attempt attempt : question.getAttempts()) {
//                if (attempt.computeResult())
//                    rate ++;
//            }
//        }
//        Result result = new Result(studentId, rate);
//    }
//
//    public int getStudentGrade(long studentId) {
//        // TODO parcourir les attempts de toutes les questions.
//        // TODO Stocker le résultat dans results.
////        for (Question question : questionSet) {
////            for (Result result : results) {
////                if(result.getUserId() == studentId && result.getValidated()) {
////                    return result.getRate();
////                }
////            }
////        }
//        return -1;
//    }
}
