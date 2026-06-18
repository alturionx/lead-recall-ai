package br.com.alturionx.lead_recall_ai_backend.event;

import java.math.BigDecimal;

public record OpportunityCreatedEvent(
        Long leadId,
        Long vehicleId,
        String phone,
        String vehicleModel,
        BigDecimal price
) implements Event {
}