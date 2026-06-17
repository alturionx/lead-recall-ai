package br.com.alturionx.lead_recall_ai_backend.integration.openai;

import org.springframework.stereotype.Service;

@Service
public class OpenAiService {

    public LeadInsight analyze(String message) {

        if (message == null || message.isBlank()) {
            return new LeadInsight(
                    "UNKNOWN",
                    null,
                    null,
                    0.0
            );
        }

        String lower = message.toLowerCase();

        // 🧠 SIMULAÇÃO INTELIGENTE (MVP RULE-BASED)
        if (lower.contains("bmw")) {

            return new LeadInsight(
                    "BUY_CAR",
                    "BMW M4",
                    extractBudget(lower),
                    0.95
            );
        }

        if (lower.contains("corolla") || lower.contains("toyota")) {

            return new LeadInsight(
                    "BUY_CAR",
                    "Toyota Corolla",
                    extractBudget(lower),
                    0.90
            );
        }

        if (lower.contains("carro") || lower.contains("veículo") || lower.contains("veiculo")) {

            return new LeadInsight(
                    "BUY_CAR",
                    null,
                    extractBudget(lower),
                    0.70
            );
        }

        // ❌ fallback
        return new LeadInsight(
                "UNKNOWN",
                null,
                null,
                0.5
        );
    }

    /**
     * 🧠 Extração simples de orçamento (MVP)
     * Ex: "até 80 mil", "100k", "500000"
     */
    private Integer extractBudget(String message) {

        try {
            if (message.contains("mil")) {

                String number = message.replaceAll("[^0-9]", "");
                if (!number.isBlank()) {
                    return Integer.parseInt(number) * 1000;
                }
            }

            if (message.contains("k")) {

                String number = message.replaceAll("[^0-9]", "");
                if (!number.isBlank()) {
                    return Integer.parseInt(number) * 1000;
                }
            }

            // fallback: tenta pegar número puro
            String number = message.replaceAll("[^0-9]", "");
            if (!number.isBlank()) {
                return Integer.parseInt(number);
            }

        } catch (Exception e) {
            return null;
        }

        return null;
    }
}