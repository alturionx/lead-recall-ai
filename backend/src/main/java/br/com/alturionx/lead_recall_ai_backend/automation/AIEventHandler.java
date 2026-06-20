package br.com.alturionx.lead_recall_ai_backend.automation;

import br.com.alturionx.lead_recall_ai_backend.event.EventBus;
import br.com.alturionx.lead_recall_ai_backend.event.LeadEnrichedEvent;
import br.com.alturionx.lead_recall_ai_backend.event.LeadEnrichmentRequestedEvent;
import br.com.alturionx.lead_recall_ai_backend.integration.openai.LeadInsight;
import br.com.alturionx.lead_recall_ai_backend.integration.openai.OpenAiService;
import br.com.alturionx.lead_recall_ai_backend.model.Lead;
import br.com.alturionx.lead_recall_ai_backend.model.Message;
import br.com.alturionx.lead_recall_ai_backend.service.LeadService;
import br.com.alturionx.lead_recall_ai_backend.service.MessageService;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AIEventHandler {

    private final EventBus eventBus;
    private final OpenAiService openAiService;
    private final LeadService leadService;
    private final MessageService messageService;

    public AIEventHandler(
            EventBus eventBus,
            OpenAiService openAiService,
            LeadService leadService,
            MessageService messageService
    ) {
        this.eventBus = eventBus;
        this.openAiService = openAiService;
        this.leadService = leadService;
        this.messageService = messageService;
    }

    @PostConstruct
    public void init() {
        eventBus.subscribe(
                LeadEnrichmentRequestedEvent.class,
                this::handle
        );
    }

    private void handle(LeadEnrichmentRequestedEvent event) {

        if (event == null
                || event.phone() == null
                || event.phone().isBlank()) {
            return;
        }

        Lead lead = leadService.findByPhone(event.phone());

        if (lead == null) {
            return;
        }

        // 🧠 Busca histórico completo do lead
        List<Message> messages =
                messageService.getLeadMessages(event.phone());

        LeadInsight insight;

        // Fallback de segurança
        if (messages == null || messages.isEmpty()) {
            insight = openAiService.analyze(event.message());
        } else {
            insight = openAiService.analyze(messages);
        }

        if (insight == null) {
            return;
        }

        // Merge inteligente
        leadService.applyInsight(lead, insight);

        // Recalcula score usando estado acumulado
        leadService.calculateScore(lead);

        // Salva
        leadService.save(lead);

        // Continua pipeline
        eventBus.publish(
                new LeadEnrichedEvent(event.phone())
        );
    }
}