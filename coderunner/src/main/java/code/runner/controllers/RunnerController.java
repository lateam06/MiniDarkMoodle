package code.runner.controllers;

import com.lateam.payload.request.CodeRequest;
import com.lateam.payload.response.CodeResponse;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.python.util.PythonInterpreter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.io.StringWriter;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/run")
public class RunnerController {



    @PostMapping("")
    public ResponseEntity<?> ComputeCode(@Valid @RequestBody CodeRequest codeRequest) throws IOException {
        try (PythonInterpreter pyInterp = new PythonInterpreter()) {
            StringWriter output = new StringWriter();
            pyInterp.setOut(output);
            pyInterp.exec(codeRequest.getStudentCode() + "\n" + codeRequest.getTest());
            CodeResponse response = new CodeResponse(output.toString().trim().equals(codeRequest.getTestResult()));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.ok(new CodeResponse(false));
        }

    }

}
