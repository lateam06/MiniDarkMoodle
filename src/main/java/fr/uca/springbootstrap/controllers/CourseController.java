package fr.uca.springbootstrap.controllers;

import fr.uca.springbootstrap.models.modules.Module;
import fr.uca.springbootstrap.models.modules.Resource;
import fr.uca.springbootstrap.models.modules.courses.Course;
import fr.uca.springbootstrap.models.modules.courses.Text;
import fr.uca.springbootstrap.models.users.User;
import fr.uca.springbootstrap.payload.request.CreateTextRequest;
import fr.uca.springbootstrap.payload.response.MessageResponse;
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
public class CourseController {

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
    TextRepository textRepository;



    private ResponseEntity<?> verifyCourseInfo(Principal principal, long module_id, long course_id) {
        Optional<User> ouser = userRepository.findByUsername(principal.getName());
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

        User user = ouser.get();
        Module module = omodule.get();
        Course course = (Course) ocourse.get();

        if (!module.getParticipants().contains(user)) {
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

    @GetMapping("/{module_id}/course/{course_id}/text/{text_id}")
    public ResponseEntity<?> getTextFromCourse(Principal principal, @PathVariable long module_id, @PathVariable long course_id, @PathVariable long text_id) {
        var resp = verifyCourseInfo(principal, module_id, course_id);
        if (resp != null) {
            return resp;
        }

        Optional<Text> otext = textRepository.findById(text_id);

        if (otext.isEmpty()) {
            return ResponseEntity
                    .notFound()
                    .build();
        }

        Course course = courseRepository.findById(course_id).get();
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

    @DeleteMapping("{module_id}/course/{course_id}/text/{text_id}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> deleteText(Principal principal, @PathVariable long module_id, @PathVariable long course_id, @PathVariable long text_id) {
        var resp = verifyCourseInfo(principal, module_id, course_id);

        if (resp != null)
            return resp;

        Optional<Text> otext = textRepository.findById(text_id);

        if (otext.isEmpty()) {
            return ResponseEntity
                    .notFound()
                    .build();
        }

        Course course = courseRepository.findById(course_id).get();
        Text text = otext.get();

        if (course.getTexts().contains(text)) {
            textRepository.delete(text);
            return ResponseEntity.accepted().body("The text has been deleted to the course.");
        }
        else {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error : This text don't belongs to this course"));
        }
    }

    //TODO DELETE TEXT

    @PostMapping("{module_id}/course/{course_id}/text")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> createNewText(Principal principal, @PathVariable long module_id, @PathVariable long course_id, @Valid @RequestBody CreateTextRequest re) {
        var resp = verifyCourseInfo(principal, module_id, course_id);

        if (resp != null)
            return resp;

        Course course = (Course) resourcesRepository.findById(course_id).get();

        Text text = new Text(re.getParagraph());
        course.getTexts().add(text);

        textRepository.save(text);

        return ResponseEntity.accepted().body("The text has been added to the course.");
    }
}
