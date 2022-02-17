package fr.uca.springbootstrap.controllers;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.validation.Valid;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lateam.payload.request.LoginRequest;
import com.lateam.payload.request.SignupRequest;
import com.lateam.payload.response.JwtResponse;
import com.lateam.payload.response.MessageResponse;
import com.lateam.payload.response.UserApiResponse;
import fr.uca.springbootstrap.models.users.ERole;
import fr.uca.springbootstrap.models.users.Role;
import fr.uca.springbootstrap.models.users.UserApi;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import fr.uca.springbootstrap.repository.RoleRepository;
import fr.uca.springbootstrap.repository.UserApiRepository;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final static String DEV_HOST = "localhost";
    private final static String PROD_HOST = "app-api-auth";

    private final static String HOST = PROD_HOST;

    @Autowired
    UserApiRepository userApiRepository;

    @Autowired
    RoleRepository roleRepository;

    private ObjectMapper ObjMapper = new ObjectMapper();
    private final CloseableHttpClient httpClient = HttpClients.createDefault();


    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) throws IOException {

        HttpPost request = new HttpPost("http://"+ HOST +":8081/api/auth/signin");
        request.addHeader("content-type", "application/json");

        request.setEntity(new StringEntity(ObjMapper.writeValueAsString(loginRequest)));

        HttpResponse response =  httpClient.execute(request);
        String bodyResponseAuthServer = EntityUtils.toString(response.getEntity());

        if (response.getStatusLine().getStatusCode() == 200) {
            JwtResponse jwtResponse = ObjMapper.readValue(bodyResponseAuthServer, JwtResponse.class);

            return ResponseEntity
                    .ok(jwtResponse);
        }
        else {
            return ResponseEntity
                    .badRequest()
                    .body(bodyResponseAuthServer);
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) throws IOException {

        if (userApiRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        HttpPost request = new HttpPost("http://"+ HOST +":8081/api/auth/signup");
        request.addHeader("content-type", "application/json");

        request.setEntity(new StringEntity(ObjMapper.writeValueAsString(signUpRequest)));
        HttpResponse response =  httpClient.execute(request);
        String bodyResponseAuthServer = EntityUtils.toString(response.getEntity());

        if (response.getStatusLine().getStatusCode() == 200) {
            UserApiResponse resp = ObjMapper.readValue(bodyResponseAuthServer, UserApiResponse.class);
            UserApi userApi = new UserApi(resp.getId(), resp.getUsername());

            Set<Role> roles = new HashSet<>();

            signUpRequest.getRole().forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);

                        break;
                    case "teacher":
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
            userApi.setRoles(roles);
            userApiRepository.save(userApi);

            return ResponseEntity.ok(resp);
        }
        else {
            return ResponseEntity
                    .badRequest()
                    .body(bodyResponseAuthServer);
        }
    }
}
