package br.com.alturionx.lead_recall_ai_backend.event;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Component
public class EventBus {

    private final List<Consumer<Object>> subscribers = new ArrayList<>();

    // publicar evento
    public void publish(Object event) {
        for (Consumer<Object> subscriber : subscribers) {
            subscriber.accept(event);
            System.out.println("Event published: " + event);
        }
    }

    // registrar listener
    public void subscribe(Consumer<Object> listener) {
        subscribers.add(listener);
    }
}