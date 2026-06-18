package br.com.alturionx.lead_recall_ai_backend.event;

import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

@Component
public class EventBus {

    private final Map<Class<?>, List<Consumer<?>>> handlers =
            new ConcurrentHashMap<>();

    public <T extends Event> void publish(T event) {

        List<Consumer<?>> list = handlers.get(event.getClass());

        if (list == null) return;

        for (Consumer<?> handler : list) {

            @SuppressWarnings("unchecked")
            Consumer<T> typed = (Consumer<T>) handler;

            typed.accept(event);
        }
    }

    public <T extends Event> void subscribe(Class<T> type,
                                            Consumer<T> handler) {

        handlers.computeIfAbsent(
                type,
                k -> new CopyOnWriteArrayList<>()
        ).add(handler);
    }
}