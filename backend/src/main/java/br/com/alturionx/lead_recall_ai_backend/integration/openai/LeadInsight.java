package br.com.alturionx.lead_recall_ai_backend.integration.openai;

public record LeadInsight(

        String intent,

        String vehicle,

        String brand,
        String model,
        String version,

        String fuelType,
        String transmission,

        Integer budget,

        Double confidence

) {}