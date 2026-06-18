package br.com.alturionx.lead_recall_ai_backend.automation;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.List;

import br.com.alturionx.lead_recall_ai_backend.event.*;
import br.com.alturionx.lead_recall_ai_backend.matcher.LeadVehicleMatcher;
import br.com.alturionx.lead_recall_ai_backend.model.*;
import br.com.alturionx.lead_recall_ai_backend.repository.*;

@Component
public class MatchingEventHandler {

    private final EventBus eventBus;
    private final LeadRepository leadRepository;
    private final VehicleRepository vehicleRepository;
    private final LeadVehicleMatcher matcher;
    private final OpportunityRepository opportunityRepository;

    public MatchingEventHandler(EventBus eventBus,
                                LeadRepository leadRepository,
                                VehicleRepository vehicleRepository,
                                LeadVehicleMatcher matcher,
                                OpportunityRepository opportunityRepository) {
        this.eventBus = eventBus;
        this.leadRepository = leadRepository;
        this.vehicleRepository = vehicleRepository;
        this.matcher = matcher;
        this.opportunityRepository = opportunityRepository;
    }

    @PostConstruct
    public void init() {
        eventBus.subscribe(LeadEnrichedEvent.class, this::onLeadUpdated);
        eventBus.subscribe(VehicleUpsertedEvent.class, this::onVehicleUpdated);
    }

    // 🚗 LEAD → VEHICLE
    private void onLeadUpdated(LeadEnrichedEvent event) {

        if (event == null || event.phone() == null) return;

        Lead lead = leadRepository.findByPhone(event.phone()).orElse(null);
        if (lead == null || lead.getVehicleInterest() == null) return;

        List<Vehicle> vehicles =
                vehicleRepository.findByModelContainingIgnoreCase(
                        lead.getVehicleInterest()
                );

        for (Vehicle vehicle : vehicles) {
            if (!matcher.matches(lead, vehicle)) continue;
            publishOpportunity(lead, vehicle);
        }
    }

    // 🚗 VEHICLE → LEAD
    private void onVehicleUpdated(VehicleUpsertedEvent event) {

        if (event == null) return;

        Vehicle vehicle = vehicleRepository.findById(event.vehicleId()).orElse(null);
        if (vehicle == null) return;

        List<Lead> leads =
                leadRepository.findByVehicleInterestContainingIgnoreCase(
                        vehicle.getModel()
                );

        for (Lead lead : leads) {
            if (!matcher.matches(lead, vehicle)) continue;
            publishOpportunity(lead, vehicle);
        }
    }

    // 🔥 CENTRALIZAÇÃO (evita duplicação)
    private void publishOpportunity(Lead lead, Vehicle vehicle) {

        if (lead == null || vehicle == null) return;

        boolean exists = opportunityRepository
                .existsByLeadIdAndVehicleId(lead.getId(), vehicle.getId());

        if (exists) return;

        System.out.println("🚗 MATCH FOUND");

        eventBus.publish(new OpportunityCreatedEvent(
                lead.getId(),
                vehicle.getId(),
                lead.getPhone(),
                vehicle.getModel(),
                vehicle.getPrice()
        ));
    }
}