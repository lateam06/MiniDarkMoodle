package fr.uca.springbootstrap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lateam.payload.request.LoginRequest;
import com.lateam.payload.request.SignupRequest;
import com.lateam.payload.response.JwtResponse;
import io.cucumber.java.en.And;
import javassist.NotFoundException;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class ConnectedStepDefs extends SpringIntegration {

    private final static String PASSWORD = "password";

    @And("{string} is connected")
    public void isConnected(String arg0) throws IOException {
        LoginRequest loginRequest = new LoginRequest(arg0, PASSWORD);

        HttpPost request = new HttpPost("http://localhost:8080/api/auth/signin");
        request.addHeader("content-type", "application/json");
        request.setEntity(new StringEntity(ObjMapper.writeValueAsString(loginRequest)));

        HttpResponse response =  httpClient.execute(request);

        assertEquals(200, response.getStatusLine().getStatusCode());

        String bodyResponseAuthServer = EntityUtils.toString(response.getEntity());
        JwtResponse jwtResponse = ObjMapper.readValue(bodyResponseAuthServer, JwtResponse.class);
        SpringIntegration.tokenHashMap.put(arg0, jwtResponse.getAccessToken());

    }
}
