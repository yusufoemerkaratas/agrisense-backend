package io.agrisense.adapter.in.web.mapper;

import io.agrisense.adapter.in.web.dto.CreateAlertRuleRequest;
import io.agrisense.adapter.in.web.dto.AlertRuleResponse;
import io.agrisense.domain.model.AlertRule;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class AlertRuleWebMapper {

    // Request DTO -> Domain
    public AlertRule toDomain(CreateAlertRuleRequest request) {
        if (request == null) return null;
        
        AlertRule rule = new AlertRule();
        rule.setRuleName(request.getName());
        rule.setCondition(request.getCondition());
        rule.setThreshold(request.getThreshold()); 
        
        // İş kuralı: Yeni kural varsayılan olarak aktiftir
        rule.setActive(true);
        
        return rule;
    }

    // Domain -> Response DTO
    public AlertRuleResponse toResponse(AlertRule domain) {
        if (domain == null) return null;

        return new AlertRuleResponse(
            domain.getId(),
            domain.getSensorId(),
            domain.getRuleName(),
            domain.getCondition(),
            domain.getThreshold(),
            domain.isActive()
        );
    }

    // Liste Dönüşümü
    public List<AlertRuleResponse> toResponseList(List<AlertRule> rules) {
        return rules.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}