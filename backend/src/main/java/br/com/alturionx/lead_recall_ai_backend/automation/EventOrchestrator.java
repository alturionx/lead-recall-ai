package br.com.alturionx.lead_recall_ai_backend.automation;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import br.com.alturionx.lead_recall_ai_backend.event.EventBus;
import br.com.alturionx.lead_recall_ai_backend.event.MessageEvent;
import br.com.alturionx.lead_recall_ai_backend.integration.openai.LeadInsight;
import br.com.alturionx.lead_recall_ai_backend.integration.openai.OpenAiService;
import br.com.alturionx.lead_recall_ai_backend.model.Lead;
import br.com.alturionx.lead_recall_ai_backend.service.MessageService;

@Component
public class EventOrchestrator {

    private final EventBus eventBus;
    private final MessageService messageService;
    private final OpenAiService openAiService;

    public EventOrchestrator(EventBus eventBus,
            MessageService messageService,
            OpenAiService openAiService) {
        this.eventBus = eventBus;
        this.messageService = messageService;
        this.openAiService = openAiService;
    }

    @PostConstruct
    public void init() {

        eventBus.subscribe(event -> {

            if (event instanceof MessageEvent messageEvent) {
                handle(messageEvent);
            }

        });
    }

    private void handle(MessageEvent event) {

        // 🧠 1. IA analisa mensagem
        LeadInsight insight = openAiService.analyze(event.message());

        System.out.println("IA RESULT: " + insight);

        // 💾 2. salva mensagem + lead
        messageService.processIncomingMessage(
                event.userId(),
                event.message());

        // 🧠 3. atualiza lead com inteligência (NOVO)
        Lead lead = messageService.findLeadByPhone(event.userId());

        if (lead != null) {

            lead.setIntent(insight.intent());
            lead.setVehicleInterest(insight.vehicle());
            lead.setBudget(insight.budget());
            lead.setConfidence(insight.confidence());

            // score simples inicial (vamos evoluir depois)
            int score = calculateScore(insight);
            lead.setScore(score);

            messageService.saveLead(lead);
        }
    }

    private int calculateScore(LeadInsight insight) {

        if (insight.intent() == null)
            return 0;

        int score = 0;

        if ("BUY_CAR".equals(insight.intent())) {
            score += 60;
        }

        if (insight.vehicle() != null) {
            score += 20;
        }

        if (insight.budget() != null && insight.budget() > 100000) {
            score += 20;
        }

        return score;
    }
}