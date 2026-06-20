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
            System.out.println(
                    "⚠️ Lead não encontrado: " + event.phone()
            );
            return;
        }

        try {

            // 🧠 Apenas histórico recente
            List<Message> messages =
                    messageService.getRecentMessages(event.phone());

            LeadInsight insight;

            if (messages == null || messages.isEmpty()) {

                System.out.println(
                        "🧠 IA sem histórico para " + event.phone()
                );

                insight = openAiService.analyze(
                        event.message()
                );

            } else {

                System.out.println(
                        "🧠 IA usando "
                                + messages.size()
                                + " mensagens de contexto"
                );

                insight = openAiService.analyze(messages);
            }

            if (insight == null) {
                return;
            }

            // 🔥 Merge inteligente
            leadService.applyInsight(
                    lead,
                    insight
            );

            // 📊 Recalcula score e heat score
            leadService.calculateScore(lead);

            // 💾 Persiste alterações
            leadService.save(lead);

            // 🚀 Continua pipeline
            eventBus.publish(
                    new LeadEnrichedEvent(event.phone())
            );

        } catch (Exception e) {

            System.out.println(
                    "❌ Erro na análise IA: "
                            + e.getMessage()
            );

            e.printStackTrace();
        }
    }
}