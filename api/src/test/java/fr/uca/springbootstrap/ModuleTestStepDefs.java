package fr.uca.springbootstrap;

import com.fasterxml.jackson.core.JsonProcessingException;
import fr.uca.springbootstrap.models.users.UserApi;
import fr.uca.springbootstrap.payload.request.CreateModuleRequest;
import fr.uca.springbootstrap.controllers.AuthController;
import fr.uca.springbootstrap.models.modules.Module;
import fr.uca.springbootstrap.payload.response.AllResourcesResponse;
import fr.uca.springbootstrap.payload.response.ModulesResponse;
import fr.uca.springbootstrap.repository.*;
import io.cucumber.java.en.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.List;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;

public class ModuleTestStepDefs extends SpringIntegration {

    private final static String PASSWORD = "password";
    private final static String BASE_URL = "http://localhost:8080/api/modules/";

    @Autowired
    ResourcesRepository resourcesRepository;

    @Autowired
    ModuleRepository moduleRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserApiRepository userRepository;

    @Autowired
    AuthController authController;

    @Autowired
    CourseRepository courseRepository;

    @Given("{string} is a teacher not registered to any module")
    public void isATeacherNotRegisteredToAnyModule(String arg0) {
        UserApi userApi = userRepository.findByUsername(arg0).get();
        List<Module> modules = userApi.getModules();

        assertTrue(modules.isEmpty());
    }

    @Given("{string} is the teacher registered to the module {string}")
    public void isTheTeacherRegisteredToTheModule(String arg0, String arg1) throws IOException {
        Module module = moduleRepository.findByName(arg1).get();
        UserApi userApi = userRepository.findByUsername(arg0).get();
        String jwt = SpringIntegration.tokenHashMap.get(arg0);
        executePost("http://localhost:8080/api/modules/" + module.getId() + "/participants/" + userApi.getId(), jwt);
    }

    @When("{string} wants to create a new Module {string}")
    public void wantsToCreateANewModule(String arg0, String arg1) throws IOException {
        CreateModuleRequest createModuleRequest = new CreateModuleRequest(arg1);
        String jwt = SpringIntegration.tokenHashMap.get(arg0);

        executePost("http://localhost:8080/api/modules", createModuleRequest, jwt);
        assertTrue(moduleRepository.findByName(arg1).isPresent());
    }

    @And("{string} has registered {string} on the module {string}")
    public void hasRegisteredOnTheModule(String teacherName, String userName, String moduleName) throws IOException {
        Module module = moduleRepository.findByName(moduleName).get();
        UserApi userApi = userRepository.findByUsername(userName).get();
        String jwt = SpringIntegration.tokenHashMap.get(teacherName);
        executePost("http://localhost:8080/api/modules/" + module.getId() + "/participants/" + userApi.getId(), jwt);
    }

    @When("{string} wants to get all resources of the module {string}")
    public void wantsToGetAllResourcesOfTheModule(String userName, String moduleName) throws IOException {
        UserApi user = userRepository.findByUsername(userName).get();
        Module module = moduleRepository.findByName(moduleName).get();

        String token = SpringIntegration.tokenHashMap.get(user.getUsername());
        String url = BASE_URL + module.getId() + "/resources";

        executeGet(url, token);
    }

    @Then("{string} gets all resources of the module {string} with at least {int} resources")
    public void getsAllResourcesOfTheModule(String userName, String moduleName, int minimum) throws JsonProcessingException {
        UserApi user = userRepository.findByUsername(userName).get();
        Module module = moduleRepository.findByName(moduleName).get();

        AllResourcesResponse arr = ObjMapper.readValue(latestJson, AllResourcesResponse.class);

        assertEquals(200, latestHttpResponse.getStatusLine().getStatusCode());
        assertTrue(arr.getResources().size() >= minimum);
    }

    @When("{string} wants to get all the modules")
    public void wantsToGetAllTheModules(String arg0) throws IOException {
        UserApi userApi = userRepository.findByUsername(arg0).get();
        String jwt = SpringIntegration.tokenHashMap.get(arg0);
        executeGet("http://localhost:8080/api/modules", jwt);
    }

    @Then("there are more than {int} modules created, {string} and {string} are part of them")
    public void thereAreMoreThanModulesCreatedAndArePartOfThem(int arg0, String arg1, String arg2) throws JsonProcessingException {
        assertEquals(200,latestHttpResponse.getStatusLine().getStatusCode());
        ModulesResponse resp = ObjMapper.readValue(latestJson, ModulesResponse.class);
        List<String> modules = resp.getModules();
        assertTrue(modules.size() >= arg0);
        Optional<Module> m1 = moduleRepository.findByName(arg1);
        Optional<Module> m2 = moduleRepository.findByName(arg2);
        assertTrue(m1.isPresent());
        assertTrue(m2.isPresent());



    }
    @And("find {string} with a description {string}")
    public void findWithADescription(String courseName, String description) throws JsonProcessingException {
        AllResourcesResponse arr = ObjMapper.readValue(latestJson, AllResourcesResponse.class);
        assertTrue(arr.getResources().size() > 0);

        boolean found = false;
        for (var x : arr.getResources()) {
            if (x.getName().equalsIgnoreCase(courseName) && x.getDescription().equalsIgnoreCase(description)) {
                found = true;
                break;
            }
        }
        assertTrue(found);
    }

    @Then("{string} can not get all resources of the module {string}")
    public void canNotGetAllResourcesOfTheModule(String arg0, String arg1) {
        assertTrue(latestHttpResponse.getStatusLine().getStatusCode() >= 400);
    }
}
