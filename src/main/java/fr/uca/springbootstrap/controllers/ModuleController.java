package fr.uca.springbootstrap.controllers;

import fr.uca.springbootstrap.models.modules.Module;
import fr.uca.springbootstrap.models.modules.Resources;
import fr.uca.springbootstrap.models.users.ERole;
import fr.uca.springbootstrap.models.users.Role;
import fr.uca.springbootstrap.models.users.User;
import fr.uca.springbootstrap.payload.request.SignupRequest;
import fr.uca.springbootstrap.payload.response.MessageResponse;
import fr.uca.springbootstrap.repository.ModuleRepository;
import fr.uca.springbootstrap.repository.ResourcesRepository;
import fr.uca.springbootstrap.repository.RoleRepository;
import fr.uca.springbootstrap.repository.UserRepository;
import fr.uca.springbootstrap.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.HashSet;
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
	ResourcesRepository ressourcesRepository;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	JwtUtils jwtUtils;

	@PostMapping("/{id}/participants/{userid}")
	@PreAuthorize("hasRole('TEACHER')")
	public ResponseEntity<?> addUser(Principal principal, @PathVariable long id, @PathVariable long userid) {
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

	//TODO
	@PostMapping("/{id}/resources/{resourcesId}")
	@PreAuthorize("hasRole('TEACHER')")
	public ResponseEntity<?> addCourse(Principal principal, @PathVariable long id, @PathVariable long resourcesId){
		Optional<Module> omodule = moduleRepository.findById(id);
		Optional<Resources> oresource = ressourcesRepository.findById(resourcesId);
		if (!omodule.isPresent()) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: No such module!"));
		}
		if (!oresource.isPresent()) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: There is already this course in this module!"));
		}

		Module module = omodule.get();
		Resources res  = oresource.get();



		Resources actorRessource = ressourcesRepository.findByName(res.getName()).get();

		Set<Resources> ressources = module.getResources();

		if ((ressources.isEmpty() && actorRessource.equals(res))
				|| ressources.contains(actorRessource)) {
			ressources.add(res);
		} else {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Not allowed to add ressource!"));
		}
		moduleRepository.save(module);
		return ResponseEntity.ok(new MessageResponse("Ressource successfully added to module!"));


	}



	@DeleteMapping("/{id}/ressources/{ressourcesId}")
	@PreAuthorize("hasRole('TEACHER')")
	public ResponseEntity<?> removeRessource(Principal principal, @PathVariable long id, @PathVariable long ressourcesId){
		Optional<Module> omodule = moduleRepository.findById(id);
		Optional<Resources> oressource = ressourcesRepository.findById(ressourcesId);
		if (!omodule.isPresent()) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: No such module!"));
		}
		if (!oressource.isPresent()) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: not a course in this module!"));
		}

		Module module = omodule.get();
		Resources res  = oressource.get();



		Resources actorRessource = ressourcesRepository.findByName(res.getName()).get();

		Set<Resources> ressources = module.getResources();

		if ((ressources.isEmpty() && actorRessource.equals(res))
				|| ressources.contains(actorRessource)) {
			ressources.remove(res);
		} else {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Not allowed to add ressource!"));
		}
		moduleRepository.save(module);
		return ResponseEntity.ok(new MessageResponse("Ressource successfully added to module!"));


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
