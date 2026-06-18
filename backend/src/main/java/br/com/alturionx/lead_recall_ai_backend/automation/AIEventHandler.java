package br.com.alturionx.lead_recall_ai_backend.automation;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import br.com.alturionx.lead_recall_ai_backend.event.*;
import br.com.alturionx.lead_recall_ai_backend.integration.openai.LeadInsight;
import br.com.alturionx.lead_recall_ai_backend.integration.openai.OpenAiService;
import br.com.alturionx.lead_recall_ai_backend.model.Lead;
import br.com.alturionx.lead_recall_ai_backend.service.LeadService;

@Component
public class AIEventHandler {

    private final EventBus eventBus;
    private final OpenAiService openAiService;
    private final LeadService leadService;

    public AIEventHandler(EventBus eventBus,
                          OpenAiService openAiService,
                          LeadService leadService) {
        this.eventBus = eventBus;
        this.openAiService = openAiService;
        this.leadService = leadService;
    }

    @PostConstruct
    public void init() {
        eventBus.subscribe(LeadEnrichmentRequestedEvent.class, this::handle);
    }

    private void handle(LeadEnrichmentRequestedEvent event) {

        if (event == null || event.phone() == null) return;

        LeadInsight insight = openAiService.analyze(event.message());

        Lead lead = leadService.findByPhone(event.phone());
        if (lead == null || insight == null) return;

        lead.setIntent(insight.intent());
        lead.setVehicleInterest(insight.vehicle());
        lead.setBudget(insight.budget());
        lead.setConfidence(insight.confidence());

        // 🔥 SCORE protegido contra null
        int score = 0;

        double confidence = insight.confidence() != null ? insight.confidence() : 0.0;

        score += (int) (confidence * 100);

        if (insight.vehicle() != null && !insight.vehicle().isBlank()) {
            score += 30;
        }

        if ("BUY_CAR".equalsIgnoreCase(insight.intent())) {
            score += 50;
        }

        lead.setScore(score);

        leadService.save(lead);

        eventBus.publish(new LeadEnrichedEvent(event.phone()));
    }
}