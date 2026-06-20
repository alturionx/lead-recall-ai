package br.com.alturionx.lead_recall_ai_backend.repository;

import br.com.alturionx.lead_recall_ai_backend.model.Lead;
import br.com.alturionx.lead_recall_ai_backend.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    /**
     * Histórico completo do lead em ordem cronológica.
     */
    List<Message> findByLeadOrderByCreatedAtAsc(Lead lead);

    /**
     * Últimas mensagens do lead.
     * Vamos usar depois para não enviar
     * conversas gigantes para a IA.
     */
    List<Message> findTop20ByLeadOrderByCreatedAtDesc(Lead lead);
}