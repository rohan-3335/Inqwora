package com.Inqwora.lexygen.Controller;


import com.Inqwora.lexygen.Body.EmailRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/email")
public class EmailGeneratorApi {


    public ResponseEntity<String> generateEmail(@RequestBody EmailRequest emailRequest){

         return ResponseEntity.ok("");
    }





}
