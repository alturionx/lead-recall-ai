package br.com.alturionx.lead_recall_ai_backend.automation;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import br.com.alturionx.lead_recall_ai_backend.event.EventBus;
import br.com.alturionx.lead_recall_ai_backend.event.MessageEvent;
import br.com.alturionx.lead_recall_ai_backend.integration.openai.LeadInsight;
import br.com.alturionx.lead_recall_ai_backend.integration.openai.LeadPriorityEngine;
import br.com.alturionx.lead_recall_ai_backend.integration.openai.OpenAiService;
import br.com.alturionx.lead_recall_ai_backend.model.Lead;
import br.com.alturionx.lead_recall_ai_backend.service.MessageService;

@Component
public class EventOrchestrator {

    private final EventBus eventBus;
    private final MessageService messageService;
    private final OpenAiService openAiService;
    private final LeadPriorityEngine leadPriorityEngine;

    public EventOrchestrator(EventBus eventBus,
                             MessageService messageService,
                             OpenAiService openAiService,
                             LeadPriorityEngine leadPriorityEngine) {

        this.eventBus = eventBus;
        this.messageService = messageService;
        this.openAiService = openAiService;
        this.leadPriorityEngine = leadPriorityEngine;
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

        // 💾 1. salva mensagem (SEM mexer no lead ainda)
        messageService.processIncomingMessage(
                event.userId(),
                event.message()
        );

        // 🧠 2. IA analisa
        LeadInsight insight = openAiService.analyze(event.message());

        System.out.println("IA RESULT: " + insight);

        // 🔎 3. carrega lead existente
        Lead lead = messageService.findLeadByPhone(event.userId());

        if (lead == null) return;

        // 🧠 4. aplica engine (REGRA CORRETA)
        leadPriorityEngine.apply(lead, insight);

        // 📊 5. score baseado em estado FINAL do lead
        lead.setScore(calculateScore(lead));

        // 💾 6. salva APENAS UMA VEZ
        messageService.saveLead(lead);
    }

    private int calculateScore(Lead lead) {

        int score = 0;

        if ("BUY_CAR".equals(lead.getIntent())) {
            score += 60;
        }

        if (lead.getVehicleInterest() != null) {
            score += 20;
        }

        if (lead.getBudget() != null && lead.getBudget() > 100000) {
            score += 20;
        }

        return score;
    }
}