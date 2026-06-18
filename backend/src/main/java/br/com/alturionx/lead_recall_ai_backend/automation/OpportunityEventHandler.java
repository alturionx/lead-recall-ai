package br.com.alturionx.lead_recall_ai_backend.automation;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import br.com.alturionx.lead_recall_ai_backend.event.*;
import br.com.alturionx.lead_recall_ai_backend.repository.OpportunityRepository;
import br.com.alturionx.lead_recall_ai_backend.repository.LeadRepository;
import br.com.alturionx.lead_recall_ai_backend.repository.VehicleRepository;
import br.com.alturionx.lead_recall_ai_backend.model.*;
import br.com.alturionx.lead_recall_ai_backend.enums.OpportunityStatus;

@Component
public class OpportunityEventHandler {

    private final EventBus eventBus;
    private final OpportunityRepository opportunityRepository;
    private final LeadRepository leadRepository;
    private final VehicleRepository vehicleRepository;

    public OpportunityEventHandler(EventBus eventBus,
                                   OpportunityRepository opportunityRepository,
                                   LeadRepository leadRepository,
                                   VehicleRepository vehicleRepository) {
        this.eventBus = eventBus;
        this.opportunityRepository = opportunityRepository;
        this.leadRepository = leadRepository;
        this.vehicleRepository = vehicleRepository;
    }

    @PostConstruct
    public void init() {
        eventBus.subscribe(OpportunityCreatedEvent.class, this::handle);
    }

    private void handle(OpportunityCreatedEvent event) {

        // 🔒 proteção contra duplicidade
        boolean exists = opportunityRepository
                .existsByLeadIdAndVehicleId(event.leadId(), event.vehicleId());

        if (exists) return;

        Lead lead = leadRepository.findById(event.leadId()).orElse(null);
        Vehicle vehicle = vehicleRepository.findById(event.vehicleId()).orElse(null);

        if (lead == null || vehicle == null) return;

        Opportunity opportunity = new Opportunity();
        opportunity.setLead(lead);
        opportunity.setVehicle(vehicle);
        opportunity.setStatus(OpportunityStatus.NEW);

        // 🔥 SCORE FINAL (CORRIGIDO E CONSISTENTE)
        int leadScore = lead.getScore() != null ? lead.getScore() : 0;
        double confidence = lead.getConfidence() != null ? lead.getConfidence() : 0.0;

        int confidenceScore = (int) (confidence * 100);

        // bônus de qualidade da oportunidade
        int intentBonus = "BUY_CAR".equals(lead.getIntent()) ? 40 : 0;

        int vehicleMatchBonus = (lead.getVehicleInterest() != null
                && vehicle.getModel() != null
                && vehicle.getModel().toLowerCase().contains(
                        lead.getVehicleInterest().toLowerCase()
                )) ? 30 : 0;

        int finalScore = leadScore
                + confidenceScore
                + intentBonus
                + vehicleMatchBonus;

        opportunity.setScore(finalScore);

        opportunityRepository.save(opportunity);

        System.out.println("🔥 OPPORTUNITY CREATED (score=" + finalScore + "): " + event.phone());
    }
}