package br.com.alturionx.lead_recall_ai_backend.integration.openai;

import br.com.alturionx.lead_recall_ai_backend.model.Message;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OpenAiService {

    private static final String API_URL =
            "https://api.groq.com/openai/v1/chat/completions";

    private static final String apiKey =
            "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";

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public OpenAiService() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Compatibilidade com versões antigas.
     */
    public LeadInsight analyze(String message) {

        return analyzeConversation(
                message == null ? "" : message
        );
    }

    /**
     * Análise contextual usando histórico.
     */
    public LeadInsight analyze(List<Message> messages) {

        if (messages == null || messages.isEmpty()) {
            return analyzeConversation("");
        }

        String conversation = messages.stream()
                .sorted(Comparator.comparing(Message::getCreatedAt))
                .limit(20)
                .map(m -> {

                    String role =
                            "OUTBOUND".equalsIgnoreCase(m.getDirection())
                                    ? "ATENDENTE"
                                    : "CLIENTE";

                    return role + ": " + m.getContent();

                })
                .collect(Collectors.joining("\n"));

        return analyzeConversation(conversation);
    }

    private LeadInsight analyzeConversation(String conversation) {

        try {

            String cleanConversation = conversation == null
                    ? ""
                    : conversation.trim();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey);

            Map<String, Object> body = Map.of(
                    "model", "llama-3.1-8b-instant",
                    "temperature", 0.1,
                    "response_format", Map.of(
                            "type",
                            "json_object"
                    ),
                    "messages", List.of(

                            Map.of(
                                    "role",
                                    "system",
                                    "content",
                                    """
                                    Você é um motor de inteligência comercial para concessionárias.

                                    Sua função é analisar TODA a conversa e manter contexto acumulado.

                                    REGRAS IMPORTANTES:

                                    1. Nunca descarte informações anteriores válidas apenas porque
                                       a última mensagem não as mencionou.

                                    2. Se o cliente disse:
                                       "Tem Corolla?"
                                       e depois:
                                       "Consegue financiar?"
                                       o veículo continua sendo Corolla.

                                    3. Se existir orçamento anterior válido,
                                       preserve o orçamento.

                                    4. Se existir intenção de compra anterior,
                                       preserve a intenção.

                                    5. Só retorne UNKNOWN quando realmente não existir
                                       informação suficiente.

                                    6. Confidence deve ser um número entre 0.0 e 1.0.

                                    Responda SOMENTE JSON válido:

                                    {
                                      "intent": "BUY_CAR | UNKNOWN",
                                      "vehicle": "string|null",
                                      "budget": number|null,
                                      "confidence": number
                                    }
                                    """
                            ),

                            Map.of(
                                    "role",
                                    "user",
                                    "content",
                                    cleanConversation
                            )
                    )
            );

            HttpEntity<Map<String, Object>> request =
                    new HttpEntity<>(body, headers);

            ResponseEntity<String> response =
                    restTemplate.exchange(
                            API_URL,
                            HttpMethod.POST,
                            request,
                            String.class
                    );

            JsonNode root =
                    objectMapper.readTree(response.getBody());

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

            LeadInsight insight =
                    objectMapper.readValue(
                            content,
                            LeadInsight.class
                    );

            if (insight.confidence() == null) {

                return new LeadInsight(
                        insight.intent(),
                        insight.vehicle(),
                        insight.budget(),
                        0.0
                );
            }

            return insight;

        } catch (Exception e) {

            System.out.println(
                    "IA ERROR: " + e.getMessage()
            );

            return new LeadInsight(
                    "UNKNOWN",
                    null,
                    null,
                    0.0
            );
        }
    }
}