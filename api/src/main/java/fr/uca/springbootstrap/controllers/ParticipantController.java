package fr.uca.springbootstrap.controllers;


import com.lateam.payload.response.MessageResponse;
import fr.uca.springbootstrap.models.modules.Module;
import fr.uca.springbootstrap.models.users.ERole;
import fr.uca.springbootstrap.models.users.Role;
import fr.uca.springbootstrap.models.users.UserApi;
import fr.uca.springbootstrap.payload.response.AllUserResponse;
import fr.uca.springbootstrap.payload.response.TeacherResponse;
import fr.uca.springbootstrap.payload.response.UserResponse;
import fr.uca.springbootstrap.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/modules")
public class ParticipantController {
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
     * #              PARTICIPANTS              #
     * ##########################################
     */

    @GetMapping("/{moduleId}/participants/{userId}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> getSingleModuleUser(@PathVariable long moduleId, @PathVariable long userId) {
        Optional<Module> omodule = moduleRepository.findById(moduleId);
        Optional<UserApi> ouser = userRepository.findById(userId);

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
        UserApi userApi = ouser.get();
        Set<UserApi> participants = module.getParticipants();
        if ((!participants.contains(userApi))) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: No such user in this module"));
        }

        UserResponse userResp = new UserResponse();
        userResp.setId(userApi.getId());
        userResp.setName(userApi.getUsername());
        List<String> roles = new ArrayList<>();
        for (Role role : userApi.getRoles()) {
            roles.add(role.getName().toString());
        }
        userResp.setRoles(roles);
        return ResponseEntity.ok(userResp);
    }

    @GetMapping("/{moduleId}/teachers")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> getModuleTeacher(Principal principal, @PathVariable long moduleId) {
        Optional<Module> omodule = moduleRepository.findById(moduleId);

        if (omodule.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: no such module"));
        }

        Module module = omodule.get();
        UserApi actor = userRepository.findByUsername(principal.getName()).get();
        Set<UserApi> participants = module.getParticipants();

        if (!participants.contains(actor)) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: not allowed to get these information"));
        }

        List<String> teachers = new ArrayList<>();
        for (UserApi participant : participants) {
            for (Role r : participant.getRoles()) {
                if (r.getName().compareTo(ERole.ROLE_TEACHER) == 0) {
                    teachers.add(participant.getUsername());
                }
            }
        }
        TeacherResponse teacherResponse = new TeacherResponse();
        teacherResponse.setTeachers(teachers);
        return ResponseEntity.ok(teacherResponse);
    }

    @GetMapping("/{moduleId}/participants")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> getAllModuleUsers(Principal principal, @PathVariable long moduleId) {
        Optional<Module> omodule = moduleRepository.findById(moduleId);

        if (omodule.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: No such module!"));
        }

        Module module = omodule.get();
        UserApi actor = userRepository.findByUsername(principal.getName()).get();
        Set<UserApi> participants = module.getParticipants();
        if (!participants.contains(actor)) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Not allowed to get information about this module"));
        }

        List<String> names = new ArrayList<>();
        List<Long> iDs = new ArrayList<>();
        List<String> roles = new ArrayList<>();

        for (UserApi participant : participants) {
            names.add(participant.getUsername());
            iDs.add(participant.getId());
            for (Role role : participant.getRoles()) {
                roles.add(role.getName().toString());
            }
        }

        AllUserResponse usersResp = new AllUserResponse();
        usersResp.setNames(names);
        usersResp.setiDs(iDs);
        usersResp.setRoles(roles);
        return ResponseEntity.ok(usersResp);
    }

    @PostMapping("/{moduleId}/participants/{userid}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> addUserToModule(Principal principal, @PathVariable long moduleId, @PathVariable long userid) {
        Optional<Module> omodule = moduleRepository.findById(moduleId);
        Optional<UserApi> ouser = userRepository.findById(userid);
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
        UserApi userApi = ouser.get();
        UserApi actor = userRepository.findByUsername(principal.getName()).get();

        Set<UserApi> participants = module.getParticipants();
        if ((participants.isEmpty() && actor.equals(userApi))
                || participants.contains(actor)) {
            participants.add(userApi);
        } else {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Not allowed to add user!"));
        }
        moduleRepository.save(module);
        return ResponseEntity.ok(new MessageResponse("User successfully added to module!"));
    }

    @DeleteMapping("/{moduleId}/participants/{userId}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> removeUser(Principal principal, @PathVariable long moduleId, @PathVariable long userId) {
        Optional<Module> omodule = moduleRepository.findById(moduleId);
        Optional<UserApi> ouser = userRepository.findById(userId);
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
        UserApi userApi = ouser.get();
        UserApi actor = userRepository.findByUsername(principal.getName()).get();

        Set<UserApi> participants = module.getParticipants();
        if ((participants.isEmpty() && actor.equals(userApi))
                || participants.contains(actor)) {
            participants.remove(userApi);
        } else {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Not allowed to remove user!"));
        }
        moduleRepository.save(module);
        return ResponseEntity.ok((new MessageResponse("User successfully remove from module")));
    }
}
