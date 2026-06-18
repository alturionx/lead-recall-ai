package br.com.alturionx.lead_recall_ai_backend.model;

import java.time.LocalDateTime;

import br.com.alturionx.lead_recall_ai_backend.enums.OpportunityStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "tb_opportunities")
@Data
public class Opportunity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Lead lead;

    @ManyToOne(optional = false)
    private Vehicle vehicle;

    @Enumerated(EnumType.STRING)
    private OpportunityStatus status;

    private Integer score; // 🔥 importante para ranking

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.status = OpportunityStatus.NEW;
        this.score = 0;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}