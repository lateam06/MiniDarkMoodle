package code.runner.controllers;

import com.lateam.payload.request.CodeRequest;
import com.lateam.payload.response.CodeResponse;


import org.python.util.PythonInterpreter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.*;
import java.util.Scanner;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/run")
public class RunnerController {



    @PostMapping("")
    public ResponseEntity<?> ComputeCode(@Valid @RequestBody CodeRequest codeRequest) throws IOException {
        try {

            String code = codeRequest.getStudentCode() + "\n" + codeRequest.getTest();

            FileWriter fw = new FileWriter("/home/temp.py");
            fw.append(code);
            fw.close();
            Process p = Runtime.getRuntime().exec("python2.7 /home/temp.py");
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));

            String result = stdInput.readLine();
            CodeResponse response = new CodeResponse(result.compareTo(codeRequest.getTestResult()) == 0);








            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.out.println("laaaaaaaaaaaaaa");
            System.out.println(e.toString());
            return ResponseEntity.ok(new CodeResponse(false));
        }

    }

}
