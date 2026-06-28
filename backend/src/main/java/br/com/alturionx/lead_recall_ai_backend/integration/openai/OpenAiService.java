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

    // ⚠️ ideal mover para env var depois
    private static final String API_KEY =
            "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";

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public OpenAiService() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    public LeadInsight analyze(String message) {
        return analyzeConversation(message == null ? "" : message);
    }

    public LeadInsight analyze(List<Message> messages) {

        if (messages == null || messages.isEmpty()) {
            return analyzeConversation("");
        }

        String conversation = messages.stream()
                .sorted(Comparator.comparing(Message::getCreatedAt))
                .limit(20)
                .map(m -> {

                    String role = "OUTBOUND".equalsIgnoreCase(m.getDirection())
                            ? "ATENDENTE"
                            : "CLIENTE";

                    return role + ": " + m.getContent();
                })
                .collect(Collectors.joining("\n"));

        return analyzeConversation(conversation);
    }

    private LeadInsight analyzeConversation(String conversation) {

        try {
            String cleanConversation =
                    conversation == null ? "" : conversation.trim();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(API_KEY);

            Map<String, Object> body = Map.of(
                    "model", "llama-3.1-8b-instant",
                    "temperature", 0.1,
                    "response_format", Map.of("type", "json_object"),
                    "messages", List.of(

                            Map.of(
                                    "role", "system",
                                    "content",
                                    """
                                        Você é um motor de inteligência comercial para concessionárias.

                                        Sua função é analisar TODA a conversa e manter contexto acumulado entre mensagens.

                                        REGRAS IMPORTANTES:

                                        1. Nunca descarte informações anteriores válidas apenas porque a última mensagem não as mencionou.

                                        2. Se o cliente mencionar um veículo (ex: Corolla, Gol, Civic), esse veículo deve ser mantido no contexto mesmo em mensagens seguintes.

                                        3. Se existir orçamento anterior válido, preserve o orçamento.

                                        4. Se existir intenção de compra anterior, preserve a intenção.

                                        5. Só retorne UNKNOWN quando realmente não houver informação suficiente na conversa.

                                        6. Confidence deve ser um número entre 0.0 e 1.0.

                                        INTENTS POSSÍVEIS:
                                        - BUY_CAR → compra de veículo
                                        - SELL_CAR → venda de veículo do cliente
                                        - RENT_CAR → aluguel/locação de veículo
                                        - INFO_ONLY → apenas pesquisa ou dúvida
                                        - UNKNOWN → sem informação suficiente

                                        REGRAS DE VEÍCULO E MARCA (IMPORTANTE):

                                        - "vehicle" é o nome do modelo mencionado (ex: Corolla, Civic, Gol).
                                        - "brand" é a marca fabricante do veículo (ex: Toyota, Honda, Volkswagen).

                                        - Quando um veículo for identificado, sempre tente inferir sua marca corretamente.
                                        - Se não tiver certeza absoluta da marca, retorne null em "brand".
                                        - NÃO invente informações que não possam ser inferidas com segurança.

                                        Exemplos:
                                        - Corolla → Toyota
                                        - Civic → Honda
                                        - Gol → Volkswagen

                                        Responda SOMENTE JSON válido no seguinte formato:

                                        {
                                        "intent": "BUY_CAR | SELL_CAR | RENT_CAR | INFO_ONLY | UNKNOWN",
                                        "vehicle": "string|null",
                                        "brand": "string|null",
                                        "model": "string|null",
                                        "version": "string|null",
                                        "fuelType": "GASOLINE | ETHANOL | FLEX | DIESEL | ELECTRIC | null",
                                        "transmission": "AUTOMATIC | MANUAL | CVT | null",
                                        "budget": number|null,
                                        "confidence": number
                                        }
                                    """
                            ),

                            Map.of(
                                    "role", "user",
                                    "content", cleanConversation
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

            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new RuntimeException("Groq API error: " + response.getStatusCode());
            }

            JsonNode root = objectMapper.readTree(response.getBody());

            String content = root.path("choices")
                    .get(0)
                    .path("message")
                    .path("content")
                    .asText();

            content = content
                    .replace("```json", "")
                    .replace("```", "")
                    .trim();

            LeadInsight insight =
                    objectMapper.readValue(content, LeadInsight.class);

            // fallback seguro
            if (insight.confidence() == null) {
                return new LeadInsight(
                        insight.intent(),
                        insight.vehicle(),
                        insight.brand(),
                        insight.model(),
                        insight.version(),
                        insight.fuelType(),
                        insight.transmission(),
                        insight.budget(),
                        0.0
                );
            }

            return insight;

        } catch (Exception e) {

            System.out.println("IA ERROR: " + e.getMessage());

            return new LeadInsight(
                    "UNKNOWN",
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    0.0
            );
        }
    }
}