package br.com.alturionx.lead_recall_ai_backend.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "tb_leads")
@NoArgsConstructor
public class Lead {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String phone;

    private String name;

    /**
     * Score comercial geral (0-100)
     */
    private Integer score = 0;

    /**
     * Campo legado.
     * Mantido para compatibilidade.
     */
    private String vehicleInterest;

    /**
     * Estruturação do interesse.
     */
    private String brand;

    private String model;

    private String version;

    private String fuelType;

    private String transmission;

    /**
     * Orçamento informado.
     */
    private Integer budget;

    /**
     * BUY_CAR, UNKNOWN...
     */
    private String intent;

    /**
     * Compatibilidade com versões antigas.
     */
    private Double confidence;

    /**
     * Confiança da última análise.
     */
    private Double currentConfidence;

    /**
     * Maior confiança já observada.
     */
    private Double maxConfidence;

    /**
     * Temperatura comercial.
     */
    private Integer heatScore = 0;

    /**
     * Última interação recebida.
     */
    private LocalDateTime lastInteractionAt;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {

        LocalDateTime now = LocalDateTime.now();

        createdAt = now;
        updatedAt = now;
        lastInteractionAt = now;

        if (score == null) {
            score = 0;
        }

        if (heatScore == null) {
            heatScore = 0;
        }

        if (currentConfidence == null) {
            currentConfidence = 0.0;
        }

        if (maxConfidence == null) {
            maxConfidence = 0.0;
        }
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}