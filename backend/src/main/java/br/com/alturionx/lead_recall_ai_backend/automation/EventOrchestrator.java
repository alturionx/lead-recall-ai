package br.com.alturionx.lead_recall_ai_backend.automation;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import br.com.alturionx.lead_recall_ai_backend.event.EventBus;
import br.com.alturionx.lead_recall_ai_backend.event.MessageEvent;
import br.com.alturionx.lead_recall_ai_backend.service.MessageService;

@Component
public class EventOrchestrator {

    private final EventBus eventBus;
    private final MessageService messageService;

    public EventOrchestrator(EventBus eventBus,
                             MessageService messageService) {
        this.eventBus = eventBus;
        this.messageService = messageService;
    }

    @PostConstruct
    public void init() {

        eventBus.subscribe(event -> {

            if (event instanceof MessageEvent messageEvent) {
                handleMessage(messageEvent);
            }

        });
    }

    private void handleMessage(MessageEvent event) {
        System.out.println("EVENT RECEIVED");
        messageService.processIncomingMessage(
                event.userId(),
                event.message()
        );
    }
}