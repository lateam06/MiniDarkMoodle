package fr.uca.springbootstrap.models.modules.questions;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.lateam.payload.request.CodeRequest;
import com.lateam.payload.response.CodeResponse;
import com.lateam.payload.response.JwtResponse;
import fr.uca.springbootstrap.repository.CodeRunnerRepository;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.python.util.PythonInterpreter;

import javax.persistence.Basic;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;
import java.io.StringWriter;

import static fr.uca.springbootstrap.controllers.AuthController.HOST_CODE_RUNNER;

@CrossOrigin(origins = "*", maxAge = 3600)

@Entity
@DiscriminatorValue("runner_attempt")
public class CodeRunnerAttempt extends Attempt {

    public CodeRunnerAttempt(Question question, Long userId) {
        super(question, userId);
    }

    public CodeRunnerAttempt() {
        super();
    }

    @Override
    public boolean computeResult() {
        try {
            CodeRunner cr = (CodeRunner) question;
            ObjectMapper ObjMapper = new ObjectMapper();
            CloseableHttpClient httpClient = HttpClients.createDefault();
            CodeRequest codeRequest = new CodeRequest(studentAttempt, cr.getTestCode(), cr.getResponse());

            HttpPost request = new HttpPost("http://" + HOST_CODE_RUNNER +":666/api/run");
            request.addHeader("content-type", "application/json");

            request.setEntity(new StringEntity(ObjMapper.writeValueAsString(codeRequest)));

            HttpResponse response = httpClient.execute(request);
            String runnerResponse = EntityUtils.toString(response.getEntity());
            CodeResponse codeResponse = ObjMapper.readValue(runnerResponse, CodeResponse.class);
            return codeResponse.isResult();


        } catch (Exception e) {
            System.out.println(e.toString());
            return false;
        }
    }

}