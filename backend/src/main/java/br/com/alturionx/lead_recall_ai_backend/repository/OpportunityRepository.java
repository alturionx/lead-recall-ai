package br.com.alturionx.lead_recall_ai_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.alturionx.lead_recall_ai_backend.model.Opportunity;

public interface OpportunityRepository
        extends JpaRepository<Opportunity, Long> {

    boolean existsByLeadIdAndVehicleId(
            Long leadId,
            Long vehicleId
    );
}