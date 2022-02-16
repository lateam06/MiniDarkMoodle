package fr.uca.springbootstrap.controllers;

import com.lateam.payload.response.MessageResponse;
import fr.uca.springbootstrap.models.modules.Module;
import fr.uca.springbootstrap.models.modules.Resource;
import fr.uca.springbootstrap.models.modules.courses.Course;
import fr.uca.springbootstrap.models.modules.courses.Text;
import fr.uca.springbootstrap.models.users.UserApi;
import fr.uca.springbootstrap.payload.request.TextRequest;
import fr.uca.springbootstrap.payload.response.TextCollectionResponse;
import fr.uca.springbootstrap.payload.response.TextResponse;
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

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/module")
public class TextController {

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
    TextRepository textRepository;

    /*
     * ##########################################
     * #             VERIFICATION               #
     * ##########################################
     */

    private ResponseEntity<?> verifyCourseInfo(Principal principal, long module_id, long course_id) {
        Optional<UserApi> ouser = userRepository.findByUsername(principal.getName());
        Optional<Module> omodule = moduleRepository.findById(module_id);
        Optional<Resource> ocourse = resourcesRepository.findById(course_id);

        if (omodule.isEmpty() || ocourse.isEmpty()) {
            return ResponseEntity
                    .notFound()
                    .build();
        }

        String discr = ocourse.get().getClass().getAnnotation(DiscriminatorValue.class).value();
        if (discr.compareTo("courses") != 0) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error : You can ony add texts to a course."));
        }

        UserApi userApi = ouser.get();
        Module module = omodule.get();
        Course course = (Course) ocourse.get();

        if (!module.getParticipants().contains(userApi)) {
            return ResponseEntity.badRequest()
                    .body("Error : The user is not registered to the module.");
        }
        if (!module.getResources().contains(course)) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error : the course don't belongs to this module."));
        }
        return null;
    }



    /*
    * ##########################################
    * #                 TEXT                   #
    * ##########################################
    */

    @GetMapping("/{moduleId}/resource/{resourceId}/text/{textId}")
    public ResponseEntity<?> getTextFromCourse(Principal principal, @PathVariable long moduleId, @PathVariable long resourceId, @PathVariable long textId) {
        var resp = verifyCourseInfo(principal, moduleId, resourceId);
        if (resp != null) {
            return resp;
        }

        Optional<Text> otext = textRepository.findById(textId);

        if (otext.isEmpty()) {
            return ResponseEntity
                    .notFound()
                    .build();
        }

        Course course = courseRepository.findById(resourceId).get();
        Text text = otext.get();

        if (course.getTexts().contains(text)) {
            return ResponseEntity
                    .ok(text);
        }
        else {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error : This text don't belongs to this course"));
        }
    }

    @GetMapping("{moduleId}/resource/{resourceId}/text")
    public ResponseEntity<?> getAllTextOfCourse(Principal principal, @PathVariable long moduleId, @PathVariable long resourceId) {
        var resp = verifyCourseInfo(principal, moduleId, resourceId);
        if (resp != null)
            return resp;

        Course course = courseRepository.findById(resourceId).get();

        List<String> listText = new ArrayList<>();

        for (Text text : course.getTexts()) {
            listText.add(text.getParagraph());
        }

        return ResponseEntity
                .ok()
                .body(new TextCollectionResponse(listText));
    }

    @PostMapping("{module_id}/resource/{resourceId}/text")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> createNewText(Principal principal, @PathVariable long module_id, @PathVariable long resourceId, @Valid @RequestBody TextRequest re) {
        var resp = verifyCourseInfo(principal, module_id, resourceId);

        if (resp != null)
            return resp;

        for (Text text : courseRepository.findById(resourceId).get().getTexts()) {
            if (text.getParagraph().equals(re.getParagraph())) {
                return ResponseEntity
                        .badRequest()
                        .body("Error : This text is already in the course.");
            }
        }

        Course course = (Course) resourcesRepository.findById(resourceId).get();
        Text text = new Text(re.getParagraph());
        course.getTexts().add(text);
        textRepository.save(text);
        courseRepository.save(course);

        return ResponseEntity.accepted().body(new TextResponse(text.getId(), text.getParagraph()));
    }


    @PutMapping("{moduleId}/resource/{resourceId}/text/{textId}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> updateText(Principal principal, @PathVariable long moduleId, @PathVariable long resourceId, @PathVariable long textId,
                                        @Valid @RequestBody TextRequest re) {
        var resp = verifyCourseInfo(principal, moduleId, resourceId);
        if (resp != null) {
            return resp;
        }
        Text text = textRepository.findById(textId)
                .orElse(new Text(re.getParagraph()));

        text.setParagraph(re.getParagraph());

        Course course = courseRepository.findById(resourceId).get();
        course.getTexts().remove(text);
        course.getTexts().add(text);

        courseRepository.save(course);
        textRepository.save(text);


        return ResponseEntity.accepted()
                .body(new TextResponse(text.getId(), text.getParagraph()));
    }

    @DeleteMapping("{moduleId}/resource/{resourceId}/text/{textId}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> deleteText(Principal principal, @PathVariable long moduleId, @PathVariable long resourceId, @PathVariable long textId) {
        var resp = verifyCourseInfo(principal, moduleId, resourceId);
        if (resp != null) {
            return resp;
        }

        var oText = textRepository.findById(textId);
        if (oText.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Text text = oText.get();
        Course course = courseRepository.findById(resourceId).get();
        course.getTexts().remove(text);

        textRepository.delete(text);
        courseRepository.save(course);

        return ResponseEntity.accepted().body(new TextResponse(text.getId(), text.getParagraph()));
    }

}
