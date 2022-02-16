package fr.uca.springbootstrap.controllers;


import com.lateam.payload.response.MessageResponse;
import fr.uca.springbootstrap.models.modules.Module;
import fr.uca.springbootstrap.models.modules.Resource;
import fr.uca.springbootstrap.models.modules.courses.Course;
import fr.uca.springbootstrap.models.modules.questions.Questionnary;
import fr.uca.springbootstrap.models.users.UserApi;
import fr.uca.springbootstrap.payload.request.ResourceRequest;
import fr.uca.springbootstrap.payload.response.AllResourcesResponse;
import fr.uca.springbootstrap.payload.response.ResourceResponse;
import fr.uca.springbootstrap.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.persistence.DiscriminatorValue;
import java.security.Principal;
import java.util.Optional;
import java.util.Set;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/modules")
public class ResourceController {
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
     * #                 RESOURCE               #
     * ##########################################
     */

    @GetMapping("/{moduleId}/resources/{resourcesId}")
    public ResponseEntity<?> getResourceByModule(@PathVariable long moduleId, @PathVariable long resourcesId) {
        Optional<Module> omodule = moduleRepository.findById(moduleId);
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

    @GetMapping("/{moduleId}/resources")
    public ResponseEntity<?> getAllResourcesOfModule(Principal principal, @PathVariable long moduleId) {
        Optional<Module> optionalModule = moduleRepository.findById(moduleId);

        if (optionalModule.isEmpty())
            return ResponseEntity.badRequest().body("Error: this module doesn't exists.");
        Module module = optionalModule.get();

        Optional<UserApi> optionalUserApi = userRepository.findByUsername(principal.getName());
        if (optionalUserApi.isEmpty())
            return ResponseEntity.badRequest().body("Error: you do not exists in the api user database.");

        UserApi user = optionalUserApi.get();
        if (!module.getParticipants().contains(user))
            return ResponseEntity.badRequest().body("Error: you're not registered in this module.");

        return ResponseEntity.ok(new AllResourcesResponse(module));
    }

    @PostMapping("/{moduleId}/resources")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> createResource(@PathVariable long moduleId, @RequestBody ResourceRequest body) {
        Optional<Module> optionalModule = moduleRepository.findById(moduleId);

        if(optionalModule.isEmpty())
            return ResponseEntity.badRequest().body("Error: this module doesn't exists.");

        Module module = optionalModule.get();
        Optional<Resource> optionalResource = resourcesRepository.findByName(body.getName());

        if(optionalResource.isPresent() && module.getResources().contains(optionalResource.get()))
            return ResponseEntity.badRequest().body("This module already contains a course with this name.");

        if (body.getType().compareTo("courses") == 0) {
            Course c = courseRepository.findByName(body.getName()).orElse(new Course());
            c.setName(body.getName());
            c.setVisibility(body.getVisibility());
            c.setDescription(body.getDescription());
            c.setTexts(body.getTexts());
            module.getResources().add(c);
            courseRepository.save(c);

            return ResponseEntity.accepted().body(new ResourceResponse(c.getId(), c.getName(), c.getDescription(), body.getType()));
        } else if (body.getType().compareTo("questionnaries") == 0) {
            Questionnary q = questionnaryRepository.findByName(body.getName()).orElse(new Questionnary());
            q.setName(body.getName());
            q.setVisibility(body.getVisibility());
            q.setDescription(body.getDescription());
            q.setQuestionSet(body.getQuestionSet());
            module.getResources().add(q);
            questionnaryRepository.save(q);

            return ResponseEntity.accepted().body(new ResourceResponse(q.getId(), q.getName(), q.getDescription(), body.getType()));
        }
        else {
            return ResponseEntity.badRequest().body("Error: Unknown resource type.");
        }
    }

    @PutMapping("/{moduleId}/resources/{resourcesId}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> updateResourceToModule(Principal principal, @PathVariable long moduleId, @PathVariable long resourcesId) {
        Optional<Module> omodule = moduleRepository.findById(moduleId);
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


    @DeleteMapping("/{moduleId}/resources/{resourcesId}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> removeResourceFromModule(@PathVariable long moduleId, @PathVariable long resourcesId) {
        Optional<Module> omodule = moduleRepository.findById(moduleId);
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

}
