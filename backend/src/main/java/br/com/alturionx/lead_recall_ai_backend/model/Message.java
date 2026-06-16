package br.com.alturionx.lead_recall_ai_backend.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    private String direction; // INBOUND / OUTBOUND

    private LocalDateTime createdAt;

    @ManyToOne
    private Lead lead;

    public Message() {
        this.createdAt = LocalDateTime.now();
    }

    // getters and setters
}