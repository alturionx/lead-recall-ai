package br.com.alturionx.lead_recall_ai_backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.alturionx.lead_recall_ai_backend.model.Lead;


public interface LeadRepository extends JpaRepository<Lead, Long> {
    Optional<Lead> findByPhone(String phone);
    List<Lead> findByVehicleInterestContainingIgnoreCase(String interest);

}