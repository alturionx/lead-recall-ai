package br.com.alturionx.lead_recall_ai_backend.event;

import java.math.BigDecimal;

public record VehicleUpsertedEvent(
        Long vehicleId,
        String brand,
        String model,
        Integer year,
        BigDecimal price
) implements Event {
}