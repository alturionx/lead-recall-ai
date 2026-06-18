package br.com.alturionx.lead_recall_ai_backend.event;

public record LeadEnrichmentRequestedEvent(
        String phone,
        String message
) implements Event {
}