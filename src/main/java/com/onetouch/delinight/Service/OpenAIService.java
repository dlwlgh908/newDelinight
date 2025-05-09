package com.onetouch.delinight.Service;

import com.onetouch.delinight.Config.OpenAiConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OpenAIService {
    private final OpenAiConfig openAiConfig;
    private final RestTemplate restTemplate = new RestTemplate();


    public String analyzeSales(String prompt){
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(openAiConfig.getApiKey());
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = new HashMap<>();
        body.put("model", openAiConfig.getModel());
        body.put("messages", List.of(Map.of("role", "user", "content", prompt)));

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
        String url = openAiConfig.getUrl();

        ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);

        List<Map<String, Object>> choices = (List<Map<String, Object>>) response.getBody().get("choices");
        Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");

        return message.get("content").toString();
    }
}
