package br.com.alturionx.lead_recall_ai_backend.event;

public record LeadEnrichedEvent(
        String phone
) implements Event {
}