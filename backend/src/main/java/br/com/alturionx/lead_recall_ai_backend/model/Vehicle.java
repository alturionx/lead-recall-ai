package br.com.alturionx.lead_recall_ai_backend.model;

import br.com.alturionx.lead_recall_ai_backend.enums.VehicleStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_vehicles")
@Data
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Toyota, Honda, Chevrolet...
     */
    private String brand;

    /**
     * Corolla, Civic, Hilux...
     */
    private String model;

    /**
     * XEi, Altis, GLi, Touring...
     */
    private String version;

    /**
     * 2023, 2024...
     */
    private Integer year;

    /**
     * Flex, Gasolina, Diesel...
     */
    private String fuelType;

    /**
     * Manual, Automático, CVT...
     */
    private String transmission;

    /**
     * Valor do veículo
     */
    private BigDecimal price;

    /**
     * Score interno do estoque.
     * Futuramente:
     * - carro parado há muito tempo
     * - promoção
     * - prioridade da loja
     */
    private Integer stockScore = 50;

    @Enumerated(EnumType.STRING)
    private VehicleStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {

        LocalDateTime now = LocalDateTime.now();

        this.createdAt = now;
        this.updatedAt = now;

        if (this.status == null) {
            this.status = VehicleStatus.AVAILABLE;
        }

        if (this.stockScore == null) {
            this.stockScore = 50;
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}