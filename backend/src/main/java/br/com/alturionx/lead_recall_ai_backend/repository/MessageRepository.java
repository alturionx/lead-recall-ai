package br.com.alturionx.lead_recall_ai_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import br.com.alturionx.lead_recall_ai_backend.model.Message;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findByLeadId(Long leadId);
}