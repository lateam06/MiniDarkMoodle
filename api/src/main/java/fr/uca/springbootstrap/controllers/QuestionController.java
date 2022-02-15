package fr.uca.springbootstrap.controllers;

import com.lateam.payload.response.MessageResponse;
import fr.uca.springbootstrap.models.modules.Module;
import fr.uca.springbootstrap.models.modules.questions.*;
import fr.uca.springbootstrap.models.users.UserApi;
import fr.uca.springbootstrap.payload.request.AnswerQCMRequest;
import fr.uca.springbootstrap.payload.request.CodeRequest;

import fr.uca.springbootstrap.payload.request.CreateNewOpenRequest;
import fr.uca.springbootstrap.payload.request.CreateNewQCMRequest;
import fr.uca.springbootstrap.payload.response.ResultResponse;
import fr.uca.springbootstrap.repository.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.*;

import javax.persistence.DiscriminatorValue;
import javax.validation.Valid;
import java.security.Principal;
import java.util.Optional;
import java.util.Set;


@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/module")
public class QuestionController {

    @Autowired
    UserApiRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    ModuleRepository moduleRepository;

    @Autowired
    ResourcesRepository resourcesRepository;

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

    @Autowired
    ResultRepository resultRepository;

    @Autowired
    QCMAttemptRepository qcmAttemptRepository;

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

        Optional<UserApi> optionalUser = userRepository.findByUsername(principal.getName());
        if (optionalUser.isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: You're not on the user database."));
        }
        UserApi userApi = optionalUser.get();

        if (!module.getParticipants().contains(userApi)) {
            return ResponseEntity.status(403).body(new MessageResponse("Error: You're not an user registered on this module."));
        }

        return null;
    }

    @PostMapping("/{moduleId}/resources/{resourcesId}/open_question")
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

    @PostMapping("/{moduleId}/resources/{resourcesId}/qcm")
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

    @GetMapping("/{moduleId}/resources/{resourcesId}/questions/{questionId}")
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

    @DeleteMapping("/{moduleId}/resources/{resourcesId}/questions/{questionId}")
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

        questionnary.getQuestionSet().remove(question);
        questionRepository.deleteById(question.getId());

        return ResponseEntity.ok("Question deleted.");
    }

    @PostMapping("/{moduleId}/resources/{resourcesId}/questions/{questionId}/code")
    public ResponseEntity<?> submitCode(Principal principal, @PathVariable long moduleId, @PathVariable long resourcesId, @PathVariable long questionId, @Valid @RequestBody CodeRequest body) {
        Optional<CodeRunner> orunner = codeRunnerRepository.findById(questionId);
        Optional<Module> omodule = moduleRepository.findById(moduleId);
        Optional<Questionnary> oquestionnary = questionnaryRepository.findById(resourcesId);
        UserApi actor = userRepository.findByUsername(principal.getName()).get();

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


    @PostMapping("/{moduleId}/resources/{questionnaryId}/qcm/{qcmId}")
    public ResponseEntity<?> answerQcm(Principal principal, @PathVariable long moduleId, @PathVariable long questionnaryId, @PathVariable long qcmId, @Valid @RequestBody AnswerQCMRequest answer) {
        var oqcm = qcmRepository.findById(qcmId);
        var omodule = moduleRepository.findById(moduleId);
        var oquestionnary = questionnaryRepository.findById(questionnaryId);

        UserApi actor = userRepository.findByUsername(principal.getName()).get();

        if (oqcm.isEmpty() || omodule.isEmpty() || oquestionnary.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Module mod = omodule.get();
        Questionnary quest = oquestionnary.get();
        QCM qcm = oqcm.get();
        if (!mod.getParticipants().contains(actor)) {
            return ResponseEntity.status(403).body(new MessageResponse("User not registered to this module"));
        }
        if (!mod.getResources().contains(quest)) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("This questionnary don't belongs to this module."));
        }
        if (!quest.getQuestionSet().contains(qcm)) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("This QCM don't belongs to this questionnary."));
        }

        var oresult = resultRepository.findByQuestionnaryIdAndUserId(questionnaryId, actor.getId());

        if (oresult.isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error : the student already validated the questionnary."));
        }
        else {
            if (!qcm.containsID(answer.getId())) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Error : this answer don't belongs to the qcm."));
            }

            QCMAttempt qcmAttempt = new QCMAttempt(qcm.getId(), actor.getId());
            qcmAttempt.setStudentAttempt(answer.getResponse());

            var oqcmAttempt = qcmAttemptRepository.findByQuestionIdAndUserId(qcm.getId(), actor.getId());
            if (oqcmAttempt.isPresent()) {
                qcm.getAttempts().remove(oqcmAttempt.get());
                qcmAttemptRepository.delete(oqcmAttempt.get());
            }

            qcm.getAttempts().add(qcmAttempt);
            qcmAttemptRepository.save(qcmAttempt);
            qcmRepository.save(qcm);
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse("QCM answered correctly"));
        }
    }

    @GetMapping("/{moduleId}/resources/{questionnaryId}/result/{userid}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> getStudentsResponses(@PathVariable long moduleId, @PathVariable long questionnaryId, @PathVariable long userid) {
        Optional<Module> omodule = moduleRepository.findById(moduleId);
        Optional<Questionnary> oquestionnary = questionnaryRepository.findById(questionnaryId);
        Optional<UserApi> ouser = userRepository.findById(userid);

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

    @PostMapping("/{moduleId}/resources/{questionnaryId}")
    public ResponseEntity<?> validateQuestionnary(Principal principal, @PathVariable long moduleId, @PathVariable long questionnaryId){
        Optional<Module> omodule = moduleRepository.findById(moduleId);
        Optional<Questionnary> oquestionnary = questionnaryRepository.findById(questionnaryId);
        UserApi actor = userRepository.findByUsername(principal.getName()).get();

        if(omodule.isEmpty() ||oquestionnary.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        Module mod = omodule.get();
        Questionnary questionnary = oquestionnary.get();
        if(!mod.getParticipants().contains(actor)){
            return ResponseEntity.status(403).body(new MessageResponse("User unregistered to the module"));
        }

        Set<Question> questionSet = questionnary.getQuestionSet();
        int rate = 0;
        for (Question question : questionSet) {
            for (Attempt attempt : question.getAttempts()) {
                if (attempt.computeResult())
                    rate ++;
            }
        }
        Result result = new Result(actor.getId(),questionnaryId, rate);
        questionnary.getResults().add(result);
        questionnaryRepository.save(questionnary);
        resultRepository.save(result);

        return ResponseEntity.accepted().body(new ResultResponse(result.getRate()));
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
