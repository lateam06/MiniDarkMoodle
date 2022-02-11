package fr.uca.springbootstrap.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.uca.springbootstrap.models.modules.Module;
import fr.uca.springbootstrap.models.modules.Resource;
import fr.uca.springbootstrap.models.modules.courses.Course;
import fr.uca.springbootstrap.models.modules.questions.Questionnary;
import fr.uca.springbootstrap.models.users.ERole;
import fr.uca.springbootstrap.models.users.Role;
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
import java.util.*;

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
    public ResponseEntity<?> getResourceByModule(@PathVariable long id, @PathVariable long resourcesId) {
        Optional<Module> omodule = moduleRepository.findById(id);
        Optional<Resource> oresource = resourcesRepository.findById(resourcesId);
        if (omodule.isEmpty()) {
            return ResponseEntity
                    .notFound().build();
        }
        if (oresource.isEmpty()) {
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
            String discriminator = res.getClass().getAnnotation(DiscriminatorValue.class).value();
            if (discriminator.compareTo("courses") == 0) {
                Course cours = courseRepository.findById(res.getId()).orElseThrow();
                return ResponseEntity.ok(cours);
            } else if (discriminator.compareTo("questionnaries") == 0) {
                Questionnary questionnary = questionnaryRepository.findById(res.getId()).orElseThrow();
                return ResponseEntity.ok(questionnary);
            } else {
                return ResponseEntity.ok(res);
            }
        }

    }


    @GetMapping("/users/{userId}")
    public ResponseEntity<?> getUsers(@PathVariable long userId) {
        Optional<User> ouser = userRepository.findById(userId);

        User us = ouser.get();
        ObjectMapper Obj = new ObjectMapper();
        return ResponseEntity.ok(us);


    }

    @GetMapping("/{moduleId}/participants/{userid}/getSingleUser")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> getSingleModuleUser(@PathVariable long moduleId, @PathVariable long userid) {
        Optional<Module> omodule = moduleRepository.findById(moduleId);
        Optional<User> ouser = userRepository.findById(userid);

        if (omodule.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: No such module!"));
        }
        if (ouser.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: No such user!"));
        }

        Module module = omodule.get();
        User user = ouser.get();
        Set<User> participants = module.getParticipants();
        if ((!participants.contains(user))) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: No such user in this module"));
        }
        return ResponseEntity.ok(user);
    }

    @GetMapping("/{moduleId}/participants/getAllUsers")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> getAllModuleUsers(Principal principal, @PathVariable long moduleId) {
        Optional<Module> omodule = moduleRepository.findById(moduleId);

        if (omodule.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: No such module!"));
        }

        Module module = omodule.get();
        User actor = userRepository.findByUsername(principal.getName()).get();
        Set<User> participants = module.getParticipants();
        if (!participants.contains(actor)) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Not allowed to get information about this module"));
        }

        return ResponseEntity.ok(module.getParticipants());
    }


    @PostMapping("/{id}/participants/{userid}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> addUserToModule(Principal principal, @PathVariable long id, @PathVariable long userid) {
        Optional<Module> omodule = moduleRepository.findById(id);
        Optional<User> ouser = userRepository.findById(userid);
        if (omodule.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: No such module!"));
        }
        if (ouser.isEmpty()) {
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
        if (omodule.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: No such module!"));
        }
        if (oresource.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: there is no course registered in database"));
        }
        Module module = omodule.get();
        Resource res = oresource.get();


        Resource actorResource = resourcesRepository.findByName(res.getName()).get();

        Set<Resource> resources = module.getResources();

        if ((resources.isEmpty() && actorResource.equals(res))
                || !resources.contains(actorResource)) {
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
    public ResponseEntity<?> addResourceToModule(@PathVariable long id, @RequestBody ResourceRequest body) {
        Optional<Module> omodule = moduleRepository.findById(id);
        if (omodule.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            Module m = omodule.get();
            System.out.println(body.getType());
            if (body.getType().compareTo("courses") == 0) {
                Course c = new Course();
                c.setName(body.getName());
                c.setVisibility(body.getVisibility());
                c.setDescription(body.getDescription());
                c.setTexts(body.getTexts());
                m.getResources().add(c);
                courseRepository.save(c);


            } else if (body.getType().compareTo("questionnaries") == 0) {
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
    public ResponseEntity<?> removeResourceFromModule(@PathVariable long id, @PathVariable long resourcesId) {
        Optional<Module> omodule = moduleRepository.findById(id);
        Optional<Resource> oresource = resourcesRepository.findById(resourcesId);
        if (omodule.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: No such module!"));
        }
        if (oresource.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: not a course in this module!"));
        }

        Module module = omodule.get();
        Resource res = oresource.get();


        Resource actorResource = resourcesRepository.findByName(res.getName()).get();

        Set<Resource> resources = module.getResources();

        if ((resources.isEmpty() && actorResource.equals(res))
                || resources.contains(actorResource)) {
            resources.remove(res);
        } else {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Not allowed to add resource!"));
        }
        moduleRepository.save(module);
        return ResponseEntity.ok(new MessageResponse("resource successfully added to module!"));

    }

    @DeleteMapping("/{moduleId}/participants/{userId}/deleteUser")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> removeUser(Principal principal, @PathVariable long moduleId, @PathVariable long userId) {
        Optional<Module> omodule = moduleRepository.findById(moduleId);
        Optional<User> ouser = userRepository.findById(userId);
        if (omodule.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: No such module"));
        }
        if (ouser.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: not such user"));
        }

        Module module = omodule.get();
        User user = ouser.get();
        User actor = userRepository.findByUsername(principal.getName()).get();

        Set<User> participants = module.getParticipants();
        if ((participants.isEmpty() && actor.equals(user))
                || participants.contains(actor)) {
            participants.remove(user);
        } else {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Not allowed to remove user!"));
        }
        moduleRepository.save(module);
        return ResponseEntity.ok((new MessageResponse("User successfully remove from module")));
    }

    @DeleteMapping("/{moduleId}/deleteModule")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> removeModule(Principal principal, @PathVariable long moduleId) {
        Optional<Module> omodule = moduleRepository.findById(moduleId);

        if (omodule.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: No such module"));
        }

        Module module = omodule.get();
        User actor = userRepository.findByUsername(principal.getName()).get();
        Set<User> participants = module.getParticipants();

        if (participants.contains(actor)) {
            moduleRepository.delete(module);
        } else {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error : not allowed to remove module"));
        }
        return ResponseEntity.ok(new MessageResponse("Module successfully remove"));
    }

    @GetMapping("/{moduleId}/getModule")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> getModuleInformation(@PathVariable long moduleId) {
        Optional<Module> omodule = moduleRepository.findById(moduleId);

        if (omodule.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: No such module!"));
        }

        Module module = omodule.get();
        ObjectMapper obj = new ObjectMapper();
        return ResponseEntity.ok(module);
    }

    @GetMapping("/{moduleId}/getTeacher")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> getModuleTeacher(Principal principal, @PathVariable long moduleId) {
        Optional<Module> omodule = moduleRepository.findById(moduleId);

        if (omodule.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: no such module"));
        }

        Module module = omodule.get();
        User actor = userRepository.findByUsername(principal.getName()).get();
        Set<User> participants = module.getParticipants();

        if (!participants.contains(actor)) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: not allowed to get these information"));
        }

        List<User> teachers = new ArrayList<>();
        for (User participant : participants) {
            for (Role r : participant.getRoles()) {
                if (r.getName().compareTo(ERole.ROLE_TEACHER) == 0) {
                    teachers.add(participant);
                }
            }
        }

        return ResponseEntity.ok(teachers);
    }

    User createUser(String userName, String email, String password, Set<String> strRoles) {
        User user = new User(userName, email, password);
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_STUDENT)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);

                        break;
                    case "mod":
                        Role modRole = roleRepository.findByName(ERole.ROLE_TEACHER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(modRole);

                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_STUDENT)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }
        user.setRoles(roles);
        return user;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account
        User user = createUser(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()), signUpRequest.getRole());
        userRepository.save(user);
        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }
}
