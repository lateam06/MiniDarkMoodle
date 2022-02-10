package fr.uca.springbootstrap;

import java.io.IOException;

import fr.uca.springbootstrap.models.modules.Resource;
import io.cucumber.spring.CucumberContextConfiguration;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
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

    @Autowired
    RestTemplate restTemplate;

    void executeGet(String url, String jwt) throws IOException {
        HttpGet request = new HttpGet(url);
        request.addHeader("Accept", "application/json");
        if (jwt != null) {
            request.addHeader("Authorization", "Bearer " + jwt);
        }

        latestHttpResponse = httpClient.execute(request);

        System.out.println("in execute get ");
        System.out.println(latestHttpResponse.toString());
    }

    void executeGet(String url) throws IOException {
        HttpGet request = new HttpGet(url);
        request.addHeader("Accept", "application/json");

        latestHttpResponse = httpClient.execute(request);

        System.out.println("in execute get ");
        System.out.println(latestHttpResponse.toString());
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