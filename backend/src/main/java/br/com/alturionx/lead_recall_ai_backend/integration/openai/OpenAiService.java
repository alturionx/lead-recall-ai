package br.com.alturionx.lead_recall_ai_backend.integration.openai;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class OpenAiService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final String API_URL =
            "https://api.groq.com/openai/v1/chat/completions";

    private static final String API_KEY =
            "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";

    public LeadInsight analyze(String message) {

        try {
            String cleanMessage = message
                    .replace("\n", " ")
                    .replace("\r", " ")
                    .trim();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(API_KEY);

            Map<String, Object> body = Map.of(
                    "model", "llama-3.1-8b-instant",
                    "temperature", 0.2,
                    "messages", List.of(
                            Map.of(
                                    "role", "system",
                                    "content",
                                    """
                                    Extraia dados do lead e responda APENAS JSON válido:

                                    {
                                      "intent": "BUY_CAR | UNKNOWN",
                                      "vehicle": "string|null",
                                      "budget": number|null,
                                      "confidence": number
                                    }

                                    Não escreva nada fora do JSON.
                                    """
                            ),
                            Map.of(
                                    "role", "user",
                                    "content", cleanMessage
                            )
                    )
            );

            HttpEntity<Map<String, Object>> request =
                    new HttpEntity<>(body, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    API_URL,
                    HttpMethod.POST,
                    request,
                    String.class
            );

            JsonNode root = objectMapper.readTree(response.getBody());

            String content = root
                    .path("choices")
                    .get(0)
                    .path("message")
                    .path("content")
                    .asText();

            content = content
                    .replace("```json", "")
                    .replace("```", "")
                    .trim();

            return objectMapper.readValue(content, LeadInsight.class);

        } catch (Exception e) {
            System.out.println("IA ERROR: " + e.getMessage());
            return new LeadInsight("UNKNOWN", null, null, 0.5);
        }
    }
}