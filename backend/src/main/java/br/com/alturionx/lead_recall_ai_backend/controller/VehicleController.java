package br.com.alturionx.lead_recall_ai_backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.alturionx.lead_recall_ai_backend.model.Vehicle;
import br.com.alturionx.lead_recall_ai_backend.repository.VehicleRepository;
import br.com.alturionx.lead_recall_ai_backend.event.EventBus;
import br.com.alturionx.lead_recall_ai_backend.event.VehicleUpsertedEvent;

@RestController
@RequestMapping("/vehicles")
public class VehicleController {

    private final VehicleRepository vehicleRepository;
    private final EventBus eventBus;

    public VehicleController(
            VehicleRepository vehicleRepository,
            EventBus eventBus) {

        this.vehicleRepository = vehicleRepository;
        this.eventBus = eventBus;
    }

    @GetMapping
    public List<Vehicle> findAll() {
        return vehicleRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Vehicle> findById(@PathVariable Long id) {
        return vehicleRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Vehicle> create(@RequestBody Vehicle vehicle) {

        Vehicle saved = vehicleRepository.save(vehicle);

        // 🚀 evento rico (evita query depois)
        eventBus.publish(
                new VehicleUpsertedEvent(
                        saved.getId(),
                        saved.getBrand(),
                        saved.getModel(),
                        saved.getYear(),
                        saved.getPrice()
                )
        );

        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Vehicle> update(
            @PathVariable Long id,
            @RequestBody Vehicle vehicle) {

        return vehicleRepository.findById(id)
                .map(existing -> {

                    existing.setBrand(vehicle.getBrand());
                    existing.setModel(vehicle.getModel());
                    existing.setYear(vehicle.getYear());
                    existing.setPrice(vehicle.getPrice());
                    existing.setStatus(vehicle.getStatus());

                    Vehicle updated = vehicleRepository.save(existing);

                    // 🚀 evento rico também no update
                    eventBus.publish(
                            new VehicleUpsertedEvent(
                                    updated.getId(),
                                    updated.getBrand(),
                                    updated.getModel(),
                                    updated.getYear(),
                                    updated.getPrice()
                            )
                    );

                    return ResponseEntity.ok(updated);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {

        if (!vehicleRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        vehicleRepository.deleteById(id);

        // (opcional futuro)
        // eventBus.publish(new VehicleDeletedEvent(id));

        return ResponseEntity.noContent().build();
    }
}