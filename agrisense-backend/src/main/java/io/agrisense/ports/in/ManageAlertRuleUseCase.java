package io.agrisense.ports.in;

import io.agrisense.domain.model.AlertRule;
import java.util.List;

public interface ManageAlertRuleUseCase {
    AlertRule createRule(Long sensorId, AlertRule rule);
    List<AlertRule> getActiveRules(Long sensorId);
}
