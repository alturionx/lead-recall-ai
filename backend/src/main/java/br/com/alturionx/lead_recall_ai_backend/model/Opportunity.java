package br.com.alturionx.lead_recall_ai_backend.model;

import br.com.alturionx.lead_recall_ai_backend.enums.OpportunityStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_opportunities")
@Data
public class Opportunity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Lead lead;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Vehicle vehicle;

    @Enumerated(EnumType.STRING)
    private OpportunityStatus status;

    /**
     * Score comercial final da oportunidade.
     */
    private Integer score;

    /**
     * Score produzido pelo matcher.
     */
    private Integer matchScore;

    /**
     * Motivo do match.
     */
    @Column(length = 500)
    private String matchReason;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {

        LocalDateTime now = LocalDateTime.now();

        this.createdAt = now;
        this.updatedAt = now;

        if (this.status == null) {
            this.status = OpportunityStatus.NEW;
        }

        if (this.score == null) {
            this.score = 0;
        }

        if (this.matchScore == null) {
            this.matchScore = 0;
        }

        if (this.matchReason == null) {
            this.matchReason = "";
        }

        if (this.matchReason.length() > 500) {
            this.matchReason =
                    this.matchReason.substring(0, 500);
        }
    }

    @PreUpdate
    public void preUpdate() {

        this.updatedAt = LocalDateTime.now();

        if (this.matchReason != null
                && this.matchReason.length() > 500) {

            this.matchReason =
                    this.matchReason.substring(0, 500);
        }
    }
}