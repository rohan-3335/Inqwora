package com.Inqwora.lexygen.Serivces;


import com.Inqwora.lexygen.Body.EmailRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
public class EmailGeneratorSerivce {


    //check .properties
      @Value("${gemini.api.url}")
      private  String apiUrl;

    @Value("${gemini.api.url}")
    private String apiKey;

    public EmailGeneratorSerivce(WebClient webClient) {
        this.webClient = webClient;
    }

    private final WebClient webClient;



     public String generateEmailReply(EmailRequest emailRequest){
         //Build the prompt
         String prompt = buildPrompt(emailRequest);

         //craft the body
         Map<String,Object> requestBody = Map.of(
                 "contents", new Object[]{
                         Map.of("parts",new Object[]{
                                 Map.of(
                                         "text", prompt
                                 )
                         })
                 }
         );


         //Do request and get response
         String response = webClient.post()
                 .uri(apiUrl +apiKey)
                 .header("Content-Type","application/json")
                 .retrieve()
                 .bodyToMono(String.class)
                 .block();

//       Extract and return the response

         return extractReplyfromResponse(response);
     }


     //check the postman and then extract the format in which data is coming and store it
    private String extractReplyfromResponse(String response) {
         try{
             ObjectMapper objectMapper = new ObjectMapper();
             JsonNode rootNode = objectMapper.readTree(response);
             return rootNode.path("candidates")
                     .get(0)
                     .path("content")
                     .path("parts")
                     .get(0)
                     .path("text")
                     .asText();

         } catch (Exception e) {
             return "ERROR in processing : " + e.getMessage();
         }

    }


    // Build Prompt from the Email we are having
    private String buildPrompt(EmailRequest emailRequest) {
         StringBuilder prompt = new StringBuilder();
          prompt.append("Generate a professional reply for the following email content. Please don't generate a subject line ");
          if(emailRequest.getTone() != null && !emailRequest.getTone().isEmpty()){
              prompt.append("Use a ").append(emailRequest.getTone()).append(" tone.");

          }
          prompt.append("\nOriginal email :\n").append(emailRequest.getEmailContent());

         return prompt.toString();
    }

}
