package br.com.alturionx.lead_recall_ai_backend.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import br.com.alturionx.lead_recall_ai_backend.enums.VehicleStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "tb_vehicles")
@Data
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String brand;

    private String model;

    private Integer year;

    private BigDecimal price;

    private String version; // 🔥 ajuda MUITO no matching (ex: XEi, GLI)

    private String fuelType; // opcional (flex, gasolina, etc)

    @Enumerated(EnumType.STRING)
    private VehicleStatus status;

    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.status = VehicleStatus.AVAILABLE;
    }
}