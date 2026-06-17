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

    private static final String API_URL = "https://api.groq.com/openai/v1/chat/completions";

    // ⚠️ ideal: colocar em application.properties
    private final String API_KEY = System.getenv("GROQ_API_KEY");

    public LeadInsight analyze(String message) {

        try {
            // 🔥 sanitiza entrada (evita quebrar JSON)
            String cleanMessage = message
                    .replace("\n", " ")
                    .replace("\r", " ")
                    .trim();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // 🔥 HEADER CORRETO GROQ
            headers.setBearerAuth(API_KEY);

            Map<String, Object> body = Map.of(
                    "model", "llama-3.1-8b-instant",
                    "temperature", 0.2,
                    "messages", List.of(
                            Map.of(
                                    "role", "system",
                                    "content",
                                    """
                                            Você é um sistema de extração de leads.
                                            Responda APENAS JSON válido no formato:
                                            {
                                              "intent": "BUY_CAR | UNKNOWN",
                                              "vehicle": "string ou null",
                                              "budget": number ou null,
                                              "confidence": number 0-1
                                            }
                                            Não inclua texto fora do JSON.
                                            """),
                            Map.of(
                                    "role", "user",
                                    "content", cleanMessage)));

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    API_URL,
                    HttpMethod.POST,
                    request,
                    String.class);

            // 🔥 parse seguro do JSON da Groq
            JsonNode root = objectMapper.readTree(response.getBody());

            String content = root
                    .path("choices")
                    .get(0)
                    .path("message")
                    .path("content")
                    .asText();

            // 🔥 remove possíveis ```json
            content = content
                    .replace("```json", "")
                    .replace("```", "")
                    .trim();

            return objectMapper.readValue(content, LeadInsight.class);

        } catch (Exception e) {
            System.out.println("IA ERROR: " + e.getMessage());

            return new LeadInsight(
                    "UNKNOWN",
                    null,
                    null,
                    0.5);
        }
    }
}