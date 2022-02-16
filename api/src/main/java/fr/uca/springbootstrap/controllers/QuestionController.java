package fr.uca.springbootstrap.controllers;

import com.lateam.payload.response.MessageResponse;
import fr.uca.springbootstrap.models.modules.Module;
import fr.uca.springbootstrap.models.modules.questions.*;
import fr.uca.springbootstrap.models.users.ERole;
import fr.uca.springbootstrap.models.users.Role;
import fr.uca.springbootstrap.models.users.UserApi;
import fr.uca.springbootstrap.payload.request.AnswerQuestionRequest;

import fr.uca.springbootstrap.payload.request.CreateQuestionRequest;
import fr.uca.springbootstrap.payload.response.AllQuestionsResponse;
import fr.uca.springbootstrap.payload.response.ResultResponse;
import fr.uca.springbootstrap.payload.response.StudentAttemptsCollectionResponse;
import fr.uca.springbootstrap.payload.response.StudentAttemptsResponse;
import fr.uca.springbootstrap.repository.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.*;

import javax.persistence.DiscriminatorValue;
import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;


@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/module")
public class QuestionController {

    @Autowired
    UserApiRepository userApiRepository;

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

    @Autowired
    AttemptRepository attemptRepository;

    /*
     * ##########################################
     * #             VERIFICATION               #
     * ##########################################
     */

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

        Optional<UserApi> optionalUser = userApiRepository.findByUsername(principal.getName());
        if (optionalUser.isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: You're not on the user database."));
        }
        UserApi userApi = optionalUser.get();

        if (!module.getParticipants().contains(userApi)) {
            return ResponseEntity.status(403).body(new MessageResponse("Error: You're not an user registered on this module."));
        }

        return null;
    }

    private ResponseEntity<?> verifyQuestion(Principal principal, long moduleId, long resourceId, long questionId) {
        var oqcm = qcmRepository.findById(questionId);
        var omodule = moduleRepository.findById(moduleId);
        var oquestionnary = questionnaryRepository.findById(resourceId);

        UserApi actor = userApiRepository.findByUsername(principal.getName()).get();

        if (oqcm.isEmpty() || omodule.isEmpty() || oquestionnary.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Module mod = omodule.get();
        Questionnary quest = oquestionnary.get();
        Question qcm = oqcm.get();
        if (!mod.getParticipants().contains(actor)) {
            return ResponseEntity.status(403).body(new MessageResponse("User not registered to this module"));
        }
        if (!mod.getResources().contains(quest)) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("This questionnary doesn't belong to this module."));
        }
        if (!quest.getQuestionSet().contains(qcm)) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("This question doesn't belong to this questionnary."));
        }

        var oresult = resultRepository.findByQuestionnaryIdAndUserId(resourceId, actor.getId());

        if (oresult.isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error : the student already validate the questionnary."));
        }
        return null;
    }


    /*
     * ##########################################
     * #          QUESTION RESOURCE             #
     * ##########################################
     */

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
                var qcmr = new CreateQuestionRequest(qcm.getName(), qcm.getDescription(), qcm.getResponse(), EQuestion.QCM, "");
                return ResponseEntity.ok(qcmr);
            case "open_questions":
                OpenQuestion op = openQuestionRepository.findById(question.getId()).get();
                var opr = new CreateQuestionRequest(op.getName(), op.getDescription(), op.getResponse(), EQuestion.OPEN, "");
                return ResponseEntity.ok(opr);
            case "code_runners":
                CodeRunner cr = codeRunnerRepository.findById(question.getId()).get();
                var crr = new CreateQuestionRequest(cr.getName(), cr.getDescription(), cr.getResponse(), EQuestion.CODE, cr.getTestCode());
                return ResponseEntity.ok(crr);
            default:
                return ResponseEntity.ok(question);
        }
    }

    @GetMapping("/{moduleId}/resources/{resourcesId}/questions")
    public ResponseEntity<?> getAllQuestion(Principal principal, @PathVariable long moduleId, @PathVariable long resourcesId) {

        var responseCheck = checkModuleQuestionnaryUser(principal, moduleId, resourcesId);
        if (responseCheck != null)
            return responseCheck;

        Questionnary questionnary = questionnaryRepository.findById(resourcesId).get();

        if (questionnary.getQuestionSet().isEmpty()) {
            return ResponseEntity.badRequest().body("Error: the questionnary doesn't contain any questions.");
        }

        List<String> questionNames = new ArrayList<>();
        for (Question question : questionnary.getQuestionSet()) {
            questionNames.add(question.getName());
        }

        AllQuestionsResponse questionsResponse = new AllQuestionsResponse();
        questionsResponse.setQuestionNames(questionNames);
        return ResponseEntity.ok(questionsResponse);
    }

    @PostMapping("/{moduleId}/resources/{resourcesId}/questions")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> createQuestion(Principal principal, @PathVariable long moduleId, @PathVariable long resourcesId,
                                            @Valid @RequestBody CreateQuestionRequest cnoRequest) {
        var responseCheck = checkModuleQuestionnaryUser(principal, moduleId, resourcesId);
        if (responseCheck != null)
            return responseCheck;

        Questionnary questionnary = questionnaryRepository.findById(resourcesId).get();

        switch (cnoRequest.getQuestionType()) {
            case OPEN:
                OpenQuestion open = openQuestionRepository.findByName(cnoRequest.getName())
                        .orElse(new OpenQuestion());

                questionnary.getQuestionSet().remove(open);

                open.setName(cnoRequest.getName());
                open.setDescription(cnoRequest.getDescription());
                open.setResponse(cnoRequest.getResponse());

                questionnary.getQuestionSet().add(open);
                openQuestionRepository.save(open);
                questionnaryRepository.save(questionnary);
                break;

            case QCM:

                QCM qcm = qcmRepository.findByName(cnoRequest.getName())
                        .orElse(new QCM());

                questionnary.getQuestionSet().remove(qcm);

                qcm.setName(cnoRequest.getName());
                qcm.setDescription(cnoRequest.getDescription());
                qcm.setResponse(cnoRequest.getResponse());

                questionnary.getQuestionSet().add(qcm);
                qcmRepository.save(qcm);
                questionnaryRepository.save(questionnary);

                break;
            case CODE:
                CodeRunner cr = codeRunnerRepository.findByName(cnoRequest.getName())
                        .orElse(new CodeRunner());

                questionnary.getQuestionSet().remove(cr);

                cr.setName(cnoRequest.getName());
                cr.setDescription(cnoRequest.getDescription());
                cr.setResponse(cnoRequest.getResponse());
                cr.setTestCode(cnoRequest.getTestCode());

                questionnary.getQuestionSet().add(cr);
                codeRunnerRepository.save(cr);
                questionnaryRepository.save(questionnary);
                break;
        }

        return ResponseEntity.ok(new MessageResponse("Question added successfully!"));
    }

    @PutMapping("/{moduleId}/resources/{resourcesId}/questions/{questionId}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> updateQuestion(Principal principal, @PathVariable long moduleId, @PathVariable long resourcesId, @PathVariable long questionId,
                                            @Valid @RequestBody CreateQuestionRequest cnoRequest) {
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

        if (optionalQuestion.isPresent()) {
            String discriminator = optionalQuestion.get().getClass().getAnnotation(DiscriminatorValue.class).value();
            switch (discriminator) {
                case "qcms":
                    if (cnoRequest.getQuestionType() != EQuestion.QCM)
                        return ResponseEntity.badRequest().body("The question exists but you gave an incorrect EType.\nExpected QCM but gave " + cnoRequest.getQuestionType().toString());
                    break;
                case "open_questions":
                    if (cnoRequest.getQuestionType() != EQuestion.OPEN)
                        return ResponseEntity.badRequest().body("The question exists but you gave an incorrect EType.\nExpected OPEN but gave " + cnoRequest.getQuestionType().toString());
                    break;
                case "code_runners":
                    if (cnoRequest.getQuestionType() != EQuestion.CODE)
                        return ResponseEntity.badRequest().body("The question exists but you gave an incorrect EType.\nExpected CODE but gave " + cnoRequest.getQuestionType().toString());
                    break;
            }
        }

        switch(cnoRequest.getQuestionType()) {
            case CODE:
                CodeRunner cr = codeRunnerRepository.findById(questionId).orElse(new CodeRunner());

                cr.setName(cnoRequest.getName());
                cr.setDescription(cnoRequest.getDescription());
                cr.setTestCode(cnoRequest.getTestCode());
                cr.setResponse(cnoRequest.getResponse());

                questionnary.getQuestionSet().remove(cr);
                questionnary.getQuestionSet().add(cr);
                codeRunnerRepository.save(cr);
                return ResponseEntity.ok("The question has been added/updated.\n" + cr.getId() + " - " + cr.getName() + " - " + cr.getResponse());
            case OPEN:
                OpenQuestion oq = openQuestionRepository.findById(questionId).orElse(new OpenQuestion());

                oq.setName(cnoRequest.getName());
                oq.setDescription(cnoRequest.getDescription());
                oq.setResponse(cnoRequest.getResponse());

                questionnary.getQuestionSet().remove(oq);
                questionnary.getQuestionSet().add(oq);
                openQuestionRepository.save(oq);
                return ResponseEntity.ok("The question has been added/updated.\n" + oq.getId() + " - " + oq.getName() + " - " + oq.getResponse());
            case QCM:
                QCM qcm = qcmRepository.findById(questionId).orElse(new QCM());

                qcm.setName(cnoRequest.getName());
                qcm.setDescription(cnoRequest.getDescription());
                qcm.setResponse(cnoRequest.getResponse());

                questionnary.getQuestionSet().remove(qcm);
                questionnary.getQuestionSet().add(qcm);
                qcmRepository.save(qcm);
                return ResponseEntity.ok("The question has been added/updated.\n" + qcm.getId() + " - " + qcm.getName() + " - " + qcm.getResponse());
            default:
                return ResponseEntity.badRequest().body("The chosen question has an invalid discriminator.");
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


    /*
     * ##########################################
     * #            VARIOUS QUESTION            #
     * ##########################################
     */

    @GetMapping("/{moduleId}/resources/{questionnaryId}/attempts/{userid}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> getStudentResponse(Principal principal, @PathVariable long moduleId, @PathVariable long questionnaryId, @PathVariable long userid) {

        var responseCheck = checkModuleQuestionnaryUser(principal, moduleId, questionnaryId);
        if (responseCheck != null)
            return responseCheck;

        Questionnary questionnary = questionnaryRepository.findById(questionnaryId).get();

        if (questionnary.getQuestionSet().isEmpty()) {
            return ResponseEntity.badRequest().body("Error: this questionnary doesn't contain any questions");
        }

        Module module = moduleRepository.findById(moduleId).get();
        var optionalUser = userApiRepository.findById(userid);

        if (optionalUser.isEmpty()) {
            return ResponseEntity.badRequest().body("Error : this user doesn't exist");
        }

        UserApi userApi = optionalUser.get();

        if (!module.getParticipants().contains(userApi)) {
            return ResponseEntity.badRequest().body("Error : this user isn't registered to this module");
        }

        StudentAttemptsResponse resp = new StudentAttemptsResponse();
        resp.setStudentAttempts(getStudentAttempts(questionnary, userApi));
        resp.setStudent(userApi);

        return ResponseEntity.ok(resp);
    }

    @GetMapping("/{moduleId}/resources/{questionnaryId}/attempts ")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> getAllStudentResponse(Principal principal, @PathVariable long moduleId, @PathVariable long questionnaryId) {

        var responseCheck = checkModuleQuestionnaryUser(principal, moduleId, questionnaryId);
        if (responseCheck != null)
            return responseCheck;

        Questionnary questionnary = questionnaryRepository.findById(questionnaryId).get();

        if (questionnary.getQuestionSet().isEmpty()) {
            return ResponseEntity.badRequest().body("Error: this questionnary doesn't contain any questions");
        }

        Module module = moduleRepository.findById(moduleId).get();
        Set<UserApi> participants = module.getParticipants();

        //List<String> attemptsResponseList = new ArrayList<>();
        List<String> studentsNames = new ArrayList<>();

        for (UserApi participant : participants) {
            for (Role role : participant.getRoles()) {
                if (role.getName().compareTo(ERole.ROLE_STUDENT) == 0) {
                    studentsNames.add(participant.getUsername());
                    //attemptsResponseList.add(getStudentAttempts(questionnary, participant));
                   /* for (Question question : questionnary.getQuestionSet()) {
                        for (Attempt attempt : question.getAttempts()) {
                            if (attempt.getUserId().equals(participant.getId())) {
                                attemptsResponseList.add(attempt.getStudentAttempt());
                            }
                        }
                    }*/
                }
            }
        }

        StudentAttemptsCollectionResponse resp = new StudentAttemptsCollectionResponse();
        //resp.setStudentAttemptsResponseList(attemptsResponseList);
        resp.setStudentsNames(studentsNames);
        return ResponseEntity.ok(resp);
    }

    @PostMapping("/{moduleId}/resources/{resourceId}/questions/{questionId}")
    public ResponseEntity<?> answerQuestion(Principal principal, @PathVariable long moduleId, @PathVariable long resourceId, @PathVariable long questionId,
                                            @Valid @RequestBody AnswerQuestionRequest answer) {

        var responseEntity = verifyQuestion(principal, moduleId, resourceId, questionId);
        if (responseEntity != null) {
            return responseEntity;
        }

        UserApi actor = userApiRepository.findByUsername(principal.getName()).get();
        Question question = questionRepository.findById(questionId).get();
        var oattempt = attemptRepository.findByQuestionIdAndUserId(questionId, actor.getId());
        if (oattempt.isPresent()) {
            question.getAttempts().remove(oattempt.get());
            attemptRepository.delete(oattempt.get());
        }

        switch (answer.getQuestionType()) {
            case QCM:
                QCM qcm = qcmRepository.findById(questionId).get();
                if (!qcm.containsID(answer.getId())) {
                    return ResponseEntity
                            .badRequest()
                            .body(new MessageResponse("Error : this answer don't belongs to the qcm."));
                }

                QCMAttempt qcmAttempt = new QCMAttempt(qcm.getId(), actor.getId());
                qcmAttempt.setStudentAttempt(answer.getResponse());

                qcm.getAttempts().add(qcmAttempt);
                qcmAttemptRepository.save(qcmAttempt);
                qcmRepository.save(qcm);

                break;

            case OPEN:

                // TODO

                break;

            case CODE:
                Optional<CodeRunner> orunner = codeRunnerRepository.findById(questionId);
                CodeRunner runner = orunner.get();

                //runner.setStudentResponse(answer.getResponse());
                codeRunnerRepository.save(runner);

                break;
        }

        return ResponseEntity
                .ok()
                .body(new MessageResponse("Question answered correctly"));
    }

    @PostMapping("/{moduleId}/resources/{questionnaryId}")
    public ResponseEntity<?> validateQuestionnary(Principal principal, @PathVariable long moduleId, @PathVariable long questionnaryId) {
        Optional<Module> omodule = moduleRepository.findById(moduleId);
        Optional<Questionnary> oquestionnary = questionnaryRepository.findById(questionnaryId);
        UserApi actor = userApiRepository.findByUsername(principal.getName()).get();

        if (omodule.isEmpty() || oquestionnary.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Module mod = omodule.get();
        Questionnary questionnary = oquestionnary.get();
        if (!mod.getParticipants().contains(actor)) {
            return ResponseEntity.status(403).body(new MessageResponse("User unregistered to the module"));
        }

        Set<Question> questionSet = questionnary.getQuestionSet();
        int rate = 0;
        for (Question question : questionSet) {
            for (Attempt attempt : question.getAttempts()) {
                if (attempt.computeResult())
                    rate++;
            }
        }
        Result result = new Result(actor.getId(), questionnaryId, rate);
        questionnary.getResults().add(result);
        questionnaryRepository.save(questionnary);
        resultRepository.save(result);

        return ResponseEntity.accepted().body(new ResultResponse(result.getRate()));
    }

    /*
     * ##########################################
     * #               FUNCTIONS                #
     * ##########################################
     */

    private List<String> getStudentAttempts(Questionnary questionnary, UserApi userApi) {
        List<String> studentAttempts = new ArrayList<>();

        for (Question question : questionnary.getQuestionSet()) {
            for (Attempt attempt : question.getAttempts()) {
                if (attempt.getUserId().equals(userApi.getId())) {
                    studentAttempts.add(attempt.getStudentAttempt());
                }
            }
        }

        return studentAttempts;
    }

//    @GetMapping("/{moduleId}/resources/{questionnaryId}/result/{userid}")
//    @PreAuthorize("hasRole('TEACHER')")
//    public ResponseEntity<?> getStudentsResponses(@PathVariable long moduleId, @PathVariable long questionnaryId, @PathVariable long userid) {
//        Optional<Module> omodule = moduleRepository.findById(moduleId);
//        Optional<Questionnary> oquestionnary = questionnaryRepository.findById(questionnaryId);
//        Optional<UserApi> ouser = userApiRepository.findById(userid);
//
//        if (omodule.isEmpty()) {
//            return ResponseEntity
//                    .badRequest()
//                    .body(new MessageResponse("Error: No such Module"));
//        }
//
//        if (oquestionnary.isEmpty()) {
//            return ResponseEntity
//                    .badRequest()
//                    .body(new MessageResponse("Error: No such questionnary in the module"));
//        }
//
//        if (ouser.isEmpty()) {
//            return ResponseEntity
//                    .badRequest()
//                    .body(new MessageResponse("Error: No such user"));
//        }
//
//        Questionnary questionnary = oquestionnary.get();
////        int grade = questionnary.getStudentGrade(userid);
////        return ResponseEntity.ok(new MessageResponse(String.format("student grade is %d", grade)));
//        return ResponseEntity.ok(new MessageResponse("student grade is ???"));
//    }
//


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
