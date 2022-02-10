package fr.uca.springbootstrap;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.uca.springbootstrap.models.modules.Resource;
import io.cucumber.spring.CucumberContextConfiguration;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.apache.http.client.methods.HttpPost;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;


@CucumberContextConfiguration
@SpringBootTest(classes = SpringBootSecurityPostgresqlApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SpringIntegration {
    static ResponseResults latestResponse = null;
    private final CloseableHttpClient httpClient = HttpClients.createDefault();
    protected HttpResponse latestHttpResponse;
    ObjectMapper ObjMapper = new ObjectMapper();

    @Autowired
    RestTemplate restTemplate;

    void executeGetdeprecated(String url, String jwt) throws IOException {
        HttpGet request = new HttpGet(url);
        request.addHeader("Accept", "application/json");
        if (jwt != null) {
            request.addHeader("Authorization", "Bearer " + jwt);
        }
        latestHttpResponse = httpClient.execute(request);
    }

    ResponseEntity<?> executeGet(String url, String jwt, Class specified){
        return restTemplate.exchange(url,
                HttpMethod.GET,
                buildHeaderFromToken(jwt),
                specified);
    }

    protected String latestJson;
    void executeGet2(String url,  String jwt) throws IOException {
        HttpGet request = new HttpGet(url);
        request.addHeader("Accept", "application/json");
        if (jwt != null) {
            request.addHeader("Authorization", "Bearer " + jwt);
        }


        latestHttpResponse = httpClient.execute(request);
        latestJson = EntityUtils.toString(latestHttpResponse.getEntity());
    }


    void executePost(String url, String jwt) throws IOException {
        HttpPost request = new HttpPost(url);
        request.addHeader("content-type", "application/json");
        if (jwt != null) {
            request.addHeader("Authorization", "Bearer " + jwt);
        }
        request.setEntity(new StringEntity("{}"));
        latestHttpResponse = httpClient.execute(request);
    }

    void executePost(String url, Object obj ,String jwt) throws IOException {
        HttpPost request = new HttpPost(url);
        request.addHeader("content-type", "application/json");
        if (jwt != null) {
            request.addHeader("Authorization", "Bearer " + jwt);
        }

        var writed = ObjMapper.writeValueAsString(obj);

        request.setEntity(new StringEntity(writed));
        latestHttpResponse = httpClient.execute(request);
    }

    HttpEntity<Object> buildHeaderFromToken(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<Object> entity = new HttpEntity<>(headers);
        return entity;
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