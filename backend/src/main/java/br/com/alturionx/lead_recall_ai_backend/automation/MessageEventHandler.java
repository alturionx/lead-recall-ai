package br.com.alturionx.lead_recall_ai_backend.automation;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import br.com.alturionx.lead_recall_ai_backend.event.*;
import br.com.alturionx.lead_recall_ai_backend.service.MessageService;

@Component
public class MessageEventHandler {

    private final EventBus eventBus;
    private final MessageService messageService;

    public MessageEventHandler(EventBus eventBus,
                               MessageService messageService) {
        this.eventBus = eventBus;
        this.messageService = messageService;
    }

    @PostConstruct
    public void init() {
        eventBus.subscribe(MessageEvent.class, this::handle);
    }

    private void handle(MessageEvent event) {

        if (event == null || event.phone() == null || event.message() == null) {
            System.out.println("⚠️ MessageEvent inválido ignorado");
            return;
        }

        // 1. persistência + criação do lead + mensagem
        messageService.processIncomingMessage(
                event.phone(),
                event.message()
        );

        // 2. dispara pipeline IA com proteção
        try {
            eventBus.publish(
                    new LeadEnrichmentRequestedEvent(
                            event.phone(),
                            event.message()
                    )
            );
        } catch (Exception e) {
            System.out.println("❌ Erro ao publicar LeadEnrichmentRequestedEvent: " + e.getMessage());
        }
    }
}