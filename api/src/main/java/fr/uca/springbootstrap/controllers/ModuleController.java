package fr.uca.springbootstrap.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lateam.payload.response.MessageResponse;
import fr.uca.springbootstrap.models.modules.Module;
import fr.uca.springbootstrap.models.modules.Resource;
import fr.uca.springbootstrap.models.modules.courses.Course;
import fr.uca.springbootstrap.models.modules.questions.Questionnary;
import fr.uca.springbootstrap.models.users.ERole;
import fr.uca.springbootstrap.models.users.Role;
import fr.uca.springbootstrap.models.users.UserApi;
import fr.uca.springbootstrap.payload.request.CreateModuleRequest;
import fr.uca.springbootstrap.payload.request.ResourceRequest;
import fr.uca.springbootstrap.payload.response.TeacherResponse;
import fr.uca.springbootstrap.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.persistence.DiscriminatorValue;
import javax.validation.Valid;
import java.security.Principal;
import java.util.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/modules")
public class ModuleController {

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

    /*
     * ##########################################
     * #                MODULE                  #
     * ##########################################
     */

    @GetMapping("/{moduleId}")
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

    @GetMapping("")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> getAllModules() {

        // TODO

        return null;
    }

    @PostMapping("")
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

    @DeleteMapping("/{moduleId}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> removeModule(Principal principal, @PathVariable long moduleId) {
        Optional<Module> omodule = moduleRepository.findById(moduleId);

        if (omodule.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: No such module"));
        }

        Module module = omodule.get();
        UserApi actor = userRepository.findByUsername(principal.getName()).get();
        Set<UserApi> participants = module.getParticipants();

        if (participants.contains(actor)) {
            moduleRepository.delete(module);
        } else {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error : not allowed to remove module"));
        }
        return ResponseEntity.ok(new MessageResponse("Module successfully remove"));
    }
}
