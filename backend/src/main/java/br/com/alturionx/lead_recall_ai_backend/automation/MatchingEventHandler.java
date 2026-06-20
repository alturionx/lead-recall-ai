package br.com.alturionx.lead_recall_ai_backend.automation;

import br.com.alturionx.lead_recall_ai_backend.event.EventBus;
import br.com.alturionx.lead_recall_ai_backend.event.LeadEnrichedEvent;
import br.com.alturionx.lead_recall_ai_backend.event.OpportunityCreatedEvent;
import br.com.alturionx.lead_recall_ai_backend.event.VehicleUpsertedEvent;
import br.com.alturionx.lead_recall_ai_backend.matcher.LeadVehicleMatcher;
import br.com.alturionx.lead_recall_ai_backend.matcher.MatchResult;
import br.com.alturionx.lead_recall_ai_backend.model.Lead;
import br.com.alturionx.lead_recall_ai_backend.model.Vehicle;
import br.com.alturionx.lead_recall_ai_backend.repository.LeadRepository;
import br.com.alturionx.lead_recall_ai_backend.repository.OpportunityRepository;
import br.com.alturionx.lead_recall_ai_backend.repository.VehicleRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MatchingEventHandler {

    private static final int MIN_OPPORTUNITY_SCORE = 50;

    private final EventBus eventBus;
    private final LeadRepository leadRepository;
    private final VehicleRepository vehicleRepository;
    private final LeadVehicleMatcher matcher;
    private final OpportunityRepository opportunityRepository;

    public MatchingEventHandler(
            EventBus eventBus,
            LeadRepository leadRepository,
            VehicleRepository vehicleRepository,
            LeadVehicleMatcher matcher,
            OpportunityRepository opportunityRepository
    ) {
        this.eventBus = eventBus;
        this.leadRepository = leadRepository;
        this.vehicleRepository = vehicleRepository;
        this.matcher = matcher;
        this.opportunityRepository = opportunityRepository;
    }

    @PostConstruct
    public void init() {

        eventBus.subscribe(
                LeadEnrichedEvent.class,
                this::onLeadUpdated
        );

        eventBus.subscribe(
                VehicleUpsertedEvent.class,
                this::onVehicleUpdated
        );
    }

    /**
     * Lead atualizado -> procura veículos compatíveis.
     */
    private void onLeadUpdated(LeadEnrichedEvent event) {

        if (event == null
                || event.phone() == null
                || event.phone().isBlank()) {
            return;
        }

        Lead lead = leadRepository.findByPhone(event.phone())
                .orElse(null);

        if (lead == null) {
            return;
        }

        if (lead.getVehicleInterest() == null
                || lead.getVehicleInterest().isBlank()) {
            return;
        }

        List<Vehicle> vehicles =
                vehicleRepository.findByModelContainingIgnoreCase(
                        lead.getVehicleInterest()
                );

        for (Vehicle vehicle : vehicles) {

            MatchResult result = matcher.match(lead, vehicle);

            if (result == null || result.score() < MIN_OPPORTUNITY_SCORE) {
                continue;
            }

            publishOpportunity(lead, vehicle, result);
        }
    }

    /**
     * Veículo atualizado -> procura leads compatíveis.
     */
    private void onVehicleUpdated(VehicleUpsertedEvent event) {

        if (event == null) {
            return;
        }

        Vehicle vehicle = vehicleRepository.findById(event.vehicleId())
                .orElse(null);

        if (vehicle == null) {
            return;
        }

        List<Lead> leads =
                leadRepository.findByVehicleInterestContainingIgnoreCase(
                        vehicle.getModel()
                );

        for (Lead lead : leads) {

            MatchResult result = matcher.match(lead, vehicle);

            if (result == null || result.score() < MIN_OPPORTUNITY_SCORE) {
                continue;
            }

            publishOpportunity(lead, vehicle, result);
        }
    }

    /**
     * Centraliza criação de oportunidade.
     */
    private void publishOpportunity(
            Lead lead,
            Vehicle vehicle,
            MatchResult result
    ) {

        if (lead == null
                || vehicle == null
                || result == null) {
            return;
        }

        boolean exists =
                opportunityRepository.existsByLeadIdAndVehicleId(
                        lead.getId(),
                        vehicle.getId()
                );

        if (exists) {
            return;
        }

        System.out.println(
                "🚗 OPPORTUNITY | score="
                        + result.score()
                        + " | lead="
                        + lead.getPhone()
                        + " | vehicle="
                        + vehicle.getModel()
        );

        System.out.println(
                "📋 Reason: "
                        + result.reason()
        );

        eventBus.publish(
                new OpportunityCreatedEvent(
                        lead.getId(),
                        vehicle.getId(),
                        lead.getPhone(),
                        vehicle.getModel(),
                        vehicle.getPrice(),
                        result.score(),
                        result.reason()
                )
        );
    }
}