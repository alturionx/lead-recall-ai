package br.com.alturionx.lead_recall_ai_backend.integration.openai;

import br.com.alturionx.lead_recall_ai_backend.model.Message;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OpenAiService {

    private static final String API_URL =
            "https://api.groq.com/openai/v1/chat/completions";

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    private static final String apiKey = "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";

    public OpenAiService() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Compatibilidade com o código atual.
     */
    public LeadInsight analyze(String message) {
        return analyzeConversation(message == null ? "" : message);
    }

    /**
     * Próxima etapa:
     * análise usando histórico de conversa.
     */
    public LeadInsight analyze(List<Message> messages) {

        String conversation = messages.stream()
                .sorted((a, b) -> a.getCreatedAt().compareTo(b.getCreatedAt()))
                .map(Message::getContent)
                .collect(Collectors.joining("\n"));

        return analyzeConversation(conversation);
    }

    private LeadInsight analyzeConversation(String conversation) {

        try {

            String cleanConversation = conversation == null
                    ? ""
                    : conversation
                    .replace("\n", " ")
                    .replace("\r", " ")
                    .trim();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey);

            Map<String, Object> body = Map.of(
                    "model", "llama-3.1-8b-instant",
                    "temperature", 0.1,
                    "response_format", Map.of(
                            "type", "json_object"
                    ),
                    "messages", List.of(
                            Map.of(
                                    "role",
                                    "system",
                                    "content",
                                    """
                                    Você é um extrator de intenção comercial para concessionárias.

                                    Analise TODA a conversa e preserve contexto já mencionado.

                                    Regras:

                                    - Se o cliente já demonstrou interesse em um veículo,
                                      mantenha esse interesse mesmo que mensagens posteriores
                                      sejam vagas.

                                    - Não remova veículo ou orçamento apenas porque a última
                                      mensagem não menciona esses dados.

                                    - Responda SOMENTE JSON válido.

                                    Formato:

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

            return objectMapper.readValue(
                    content,
                    LeadInsight.class
            );

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