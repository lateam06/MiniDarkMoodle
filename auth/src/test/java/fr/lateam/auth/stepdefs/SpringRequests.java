package fr.lateam.auth.stepdefs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lateam.payload.response.JwtResponse;
import fr.lateam.auth.springbootsrap.AuthApplication;
import io.cucumber.spring.CucumberContextConfiguration;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.io.IOException;

@CucumberContextConfiguration
@SpringBootTest(classes = AuthApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SpringRequests {

    protected JwtResponse lastJwtReponse;
    protected HttpResponse latestHttpResponse;
    protected String latestJson = "";
    protected ObjectMapper ObjMapper = new ObjectMapper();
    protected final CloseableHttpClient httpClient = HttpClients.createDefault();

    void executeGet(String url,  String jwt) throws IOException {
        HttpGet request = new HttpGet(url);
        request.addHeader("Accept", "application/json");
        if (jwt != null) {
            request.addHeader("Authorization", "Bearer " + jwt);
        }

        latestHttpResponse = httpClient.execute(request);
        latestJson = EntityUtils.toString(latestHttpResponse.getEntity());

    }

    void executePut(String url, String jwt) throws IOException {
        HttpPut request = new HttpPut(url);
        request.addHeader("Accept", "application/json");
        if (jwt != null) {
            request.addHeader("Authorization", "Bearer " + jwt);
        }

        latestHttpResponse = httpClient.execute(request);
        latestJson = EntityUtils.toString(latestHttpResponse.getEntity());
    }

    void executePut(String url, Object obj, String jwt) throws IOException {
        if (latestHttpResponse != null) {
            EntityUtils.consume(latestHttpResponse.getEntity());
        }
        HttpPut request = new HttpPut(url);
        request.addHeader("content-type", "application/json");
        if (jwt != null) {
            request.addHeader("Authorization", "Bearer " + jwt);
        }

        request.setEntity(new StringEntity(ObjMapper.writeValueAsString(obj)));
        latestHttpResponse = httpClient.execute(request);
    }

    void executePost(String url, String jwt) throws IOException {
        HttpPost request = new HttpPost(url);
        request.addHeader("content-type", "application/json");
        if (jwt != null) {
            request.addHeader("Authorization", "Bearer " + jwt);
        }
        request.setEntity(new StringEntity("{}"));
        latestHttpResponse = httpClient.execute(request);
        latestJson = EntityUtils.toString(latestHttpResponse.getEntity());
    }

    void executePost(String url, Object obj ,String jwt) throws IOException {
        if (latestHttpResponse != null) {
            EntityUtils.consume(latestHttpResponse.getEntity());
        }
        HttpPost request = new HttpPost(url);
        request.addHeader("content-type", "application/json");
        if (jwt != null) {
            request.addHeader("Authorization", "Bearer " + jwt);
        }

        request.setEntity(new StringEntity(ObjMapper.writeValueAsString(obj)));
        latestHttpResponse = httpClient.execute(request);
    }

    HttpEntity<Object> buildHeaderFromToken(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + token);
        return new HttpEntity<>(headers);
    }

    void executeDelete(String url, String jwt) throws IOException{
        HttpDelete request= new HttpDelete(url);
        request.addHeader("content-type", "application/json");
        if (jwt != null) {
            request.addHeader("Authorization", "Bearer " + jwt);
        }
        latestHttpResponse = httpClient.execute(request);
    }

}
