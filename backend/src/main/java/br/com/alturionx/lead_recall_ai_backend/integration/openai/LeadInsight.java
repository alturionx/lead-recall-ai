package br.com.alturionx.lead_recall_ai_backend.integration.openai;

public record LeadInsight(
        String intent,
        String vehicle,
        Integer budget,
        Double confidence
) {}