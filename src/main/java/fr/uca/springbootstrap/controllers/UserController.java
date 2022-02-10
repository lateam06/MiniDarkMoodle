package fr.uca.springbootstrap.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import fr.uca.springbootstrap.models.modules.Module;
import fr.uca.springbootstrap.models.users.User;
import fr.uca.springbootstrap.payload.response.MessageResponse;
import fr.uca.springbootstrap.repository.*;
import fr.uca.springbootstrap.security.services.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/module")
public class UserController {
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

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<?> getUsersModule(@PathVariable long userId) throws JsonProcessingException {
        Optional<User> ouser = userRepository.findById(userId);

        if (ouser.isEmpty()) {
            return ResponseEntity
                    .notFound()
                    .build();
        }

        User us = ouser.get();

        return ResponseEntity.ok(us.getModules());
    }

    @GetMapping("/{id}/users")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> getAllStudentFromModule(Principal principal, @PathVariable long id) {
        Optional<Module> omodule = moduleRepository.findById(id);

        if (omodule.isEmpty()) {
            return ResponseEntity
                    .notFound()
                    .build();
        }

        Module module = omodule.get();
        User actor = userRepository.findByUsername(principal.getName()).get();

        if (module.getParticipants().contains(actor)) {
            return ResponseEntity
                    .ok(module.getParticipants());
        }
        else {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error : You are not allowed to see this."));
        }
    }

}
