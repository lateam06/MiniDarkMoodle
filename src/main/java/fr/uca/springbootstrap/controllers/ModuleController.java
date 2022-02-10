package fr.uca.springbootstrap.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import fr.uca.springbootstrap.models.modules.Module;
import fr.uca.springbootstrap.models.modules.Resource;
import fr.uca.springbootstrap.models.modules.courses.Course;
import fr.uca.springbootstrap.models.modules.questions.Questionnary;
import fr.uca.springbootstrap.models.users.User;
import fr.uca.springbootstrap.payload.request.CreateModuleRequest;
import fr.uca.springbootstrap.payload.request.ResourceRequest;
import fr.uca.springbootstrap.payload.request.SignupRequest;
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
import java.util.Set;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/module")
public class ModuleController {

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


    @GetMapping("/{id}/resources/{resourcesId}")
    public ResponseEntity<?> getResourceByModule(@PathVariable long id, @PathVariable long resourcesId) throws JsonProcessingException {
        Optional<Module> omodule = moduleRepository.findById(id);
        Optional<Resource> oresource = resourcesRepository.findById(resourcesId);
        if (!omodule.isPresent()) {
            return ResponseEntity
                    .notFound().build();
        }
        if (!oresource.isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: there is no course registered in database"));
        }

        Module module = omodule.get();
        Resource res = oresource.get();


        if (!module.getResources().contains(res)) {
            return ResponseEntity
                    .notFound().build();

        } else {
            String discriminator  =res.getClass().getAnnotation(DiscriminatorValue.class).value();
            if (discriminator.compareTo("courses") == 0 ){
                Course cours = courseRepository.findById(res.getId()).orElseThrow();
                return ResponseEntity.ok(cours);
            }
            else if (discriminator.compareTo("questionnaries") == 0 ){
                Questionnary questionnary = questionnaryRepository.findById(res.getId()).orElseThrow();
                return ResponseEntity.ok(questionnary);
            }
            else{
                return ResponseEntity.ok(res);
            }
        }

    }

    @PostMapping("/{id}/participants/{userid}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> addUserToModule(Principal principal, @PathVariable long id, @PathVariable long userid) {
        Optional<Module> omodule = moduleRepository.findById(id);
        Optional<User> ouser = userRepository.findById(userid);
        if (!omodule.isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: No such module!"));
        }
        if (!ouser.isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: No such user!"));
        }

        Module module = omodule.get();
        User user = ouser.get();
        User actor = userRepository.findByUsername(principal.getName()).get();

        Set<User> participants = module.getParticipants();
        if ((participants.isEmpty() && actor.equals(user))
                || participants.contains(actor)) {
            participants.add(user);
        } else {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Not allowed to add user!"));
        }
        moduleRepository.save(module);
        return ResponseEntity.ok(new MessageResponse("User successfully added to module!"));
    }

    @PostMapping("/createModule")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> createNewModule(@Valid @RequestBody CreateModuleRequest moduleRequest) {
        Optional<Module> omodule = moduleRepository.findByName(moduleRequest.getName());

        if (omodule.isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error : This module already exists."));
        }

        Module module = new Module(moduleRequest.getName());
        moduleRepository.save(module);

        return ResponseEntity.accepted().body(new MessageResponse("Module successfully created"));
    }

    @PostMapping("/{id}/resources/{resourcesId}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> addResourceToModule(Principal principal, @PathVariable long id, @PathVariable long resourcesId) {
        Optional<Module> omodule = moduleRepository.findById(id);
        Optional<Resource> oresource = resourcesRepository.findById(resourcesId);
        if (!omodule.isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: No such module!"));
        }
        if (!oresource.isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: there is no course registered in database"));
        }
        Module module = omodule.get();
        Resource res = oresource.get();



        Resource actorresource = resourcesRepository.findByName(res.getName()).get();

        Set<Resource> resources = module.getResources();

        if ((resources.isEmpty() && actorresource.equals(res))
                || !resources.contains(actorresource)) {
            resources.add(res);
        } else {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Not allowed to add resource!"));
        }
        moduleRepository.save(module);
        return ResponseEntity.ok(new MessageResponse("resource successfully added to module!"));


    }


    @PostMapping("/{id}/resources/")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> addRessourceToModule(Principal principal,@PathVariable long id, @RequestBody ResourceRequest body){
        Optional<Module> omodule = moduleRepository.findById(id);
        if(omodule.isEmpty()){
            return ResponseEntity.notFound().build();
        }


        else{
            Module m = omodule.get();
            System.out.println(body.getType());
            if(body.getType().compareTo("courses") == 0 ){
                Course c = new Course();
                c.setName(body.getName());
                c.setVisibility(body.getVisibility());
                c.setDescription(body.getDescription());
                c.setTexts(body.getTexts());
                m.getResources().add(c);
                courseRepository.save(c);


            }
            else if (body.getType().compareTo("questionnaries") == 0 ){
                Questionnary q = new Questionnary();
                q.setName(body.getName());
                q.setVisibility(body.getVisibility());
                q.setDescription(body.getDescription());
                q.setQuestionSet(body.getQuestionSet());
                m.getResources().add(q);
                questionnaryRepository.save(q);

            }
            return ResponseEntity.accepted().build();



        }

    }




    @DeleteMapping("/{id}/resources/{resourcesId}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> removeResourceFromModule(Principal principal, @PathVariable long id, @PathVariable long resourcesId) {
        Optional<Module> omodule = moduleRepository.findById(id);
        Optional<Resource> oresource = resourcesRepository.findById(resourcesId);
        if (!omodule.isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: No such module!"));
        }
        if (!oresource.isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: not a course in this module!"));


        }

        Module module = omodule.get();
        Resource res = oresource.get();


        Resource actorresource = resourcesRepository.findByName(res.getName()).get();

        Set<Resource> resources = module.getResources();

        if ((resources.isEmpty() && actorresource.equals(res))
                || resources.contains(actorresource)) {
            resources.remove(res);
        } else {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Not allowed to add resource!"));
        }
        moduleRepository.save(module);
        return ResponseEntity.ok(new MessageResponse("resource successfully added to module!"));

    }
}
