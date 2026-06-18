package br.com.alturionx.lead_recall_ai_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.alturionx.lead_recall_ai_backend.model.Vehicle;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    List<Vehicle> findByModelContainingIgnoreCase(String model);
}