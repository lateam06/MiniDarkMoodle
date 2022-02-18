package fr.uca.springbootstrap.controllers;


import com.lateam.payload.response.MessageResponse;
import fr.uca.springbootstrap.models.modules.Module;
import fr.uca.springbootstrap.models.users.UserApi;
import fr.uca.springbootstrap.payload.request.CreateModuleRequest;
import fr.uca.springbootstrap.payload.response.ModuleResponse;
import fr.uca.springbootstrap.payload.response.ModulesResponse;
import fr.uca.springbootstrap.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
        Optional<Module> optionalModule = moduleRepository.findById(moduleId);

        if (optionalModule.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: No such module!"));
        }

        return ResponseEntity.ok(new ModuleResponse(optionalModule.get()));
    }

    @GetMapping("")
    public ResponseEntity<?> getAllModules(Principal principal) {
        List<Module> modules = moduleRepository.findAll();
        if (modules.size() == 0) {
            return ResponseEntity.status(204).body(new MessageResponse("No module registered"));
        } else {
            ModulesResponse resp = new ModulesResponse(modules);
            return ResponseEntity.ok(resp);

        }
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

        return ResponseEntity.accepted().body(new ModuleResponse(module));
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
        return ResponseEntity.ok(new MessageResponse("Module " + module.getId() + " named " + module.getName() + " deleted."));
    }
}
