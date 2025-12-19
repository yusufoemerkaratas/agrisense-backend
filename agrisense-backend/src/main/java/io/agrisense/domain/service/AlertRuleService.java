package io.agrisense.domain.service;

import java.util.List;

import io.agrisense.domain.model.AlertRule;
import io.agrisense.domain.model.Sensor;
import io.agrisense.ports.out.IAlertRuleRepository;
import io.agrisense.ports.out.ISensorRepository;
import io.agrisense.ports.in.IManageAlertRuleUseCase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

/**
 * Domain Service for UC-03: Define Alert Rule
 * Handles business logic for creating alert rules
 */
@ApplicationScoped
public class AlertRuleService implements IManageAlertRuleUseCase {
    
    private final ISensorRepository sensorRepository;
    private final IAlertRuleRepository alertRuleRepository;
    
    public AlertRuleService(ISensorRepository sensorRepository, 
                           IAlertRuleRepository alertRuleRepository) {
        this.sensorRepository = sensorRepository;
        this.alertRuleRepository = alertRuleRepository;
    }
       //interfaceten elen ilk metot
    @Transactional
    public AlertRule createRule(Long sensorId, AlertRule rule) {
        // Validate input parameters
        if (sensorId == null) {
            throw new IllegalArgumentException("Sensor ID cannot be null");
        }
        if (rule == null) {
            throw new IllegalArgumentException("Alert rule cannot be null");
        }
        
        // Ensure sensor exists
        Sensor sensor = sensorRepository.findById(sensorId);
        if (sensor == null) {
            throw new IllegalArgumentException("Sensor with ID " + sensorId + " not found");
        }

        // Minimal ownership check: sensor must be linked to a field (and thus owned by a farmer via that field)
        if (sensor.getFieldId() == null) {
            throw new IllegalArgumentException("Sensor with ID " + sensorId + " has no field assigned");
        }

        // Assign sensorId to rule and persist
        rule.setSensorId(sensorId);
        return alertRuleRepository.save(rule);
    }
    // interface ikinci metot
    /**
     * Get active rules for a sensor
     * UC-03: Allow farmer to view active alert rules
     */
    public List<AlertRule> getActiveRules(Long sensorId) {
        // Validate input parameter
        if (sensorId == null) {
            throw new IllegalArgumentException("Sensor ID cannot be null");
        }
        
        // Ensure sensor exists
        Sensor sensor = sensorRepository.findById(sensorId);
        if (sensor == null) {
            throw new IllegalArgumentException("Sensor with ID " + sensorId + " not found");
        }
        
        // Get active rules for the sensor
        return alertRuleRepository.findActiveBySensorId(sensorId);
    }

    @Override
    @Transactional
    public AlertRule updateRule(Long ruleId, AlertRule updatedRule) {
        if (ruleId == null) {
            throw new IllegalArgumentException("Rule ID cannot be null");
        }
        if (updatedRule == null) {
            throw new IllegalArgumentException("Updated rule cannot be null");
        }

        AlertRule existing = alertRuleRepository.findById(ruleId);
        if (existing == null) {
            throw new IllegalArgumentException("Alert rule with ID " + ruleId + " not found");
        }

        // Update fields
        existing.setCondition(updatedRule.getCondition());
        existing.setThreshold(updatedRule.getThreshold());
        existing.setRuleName(updatedRule.getRuleName());
        
        return alertRuleRepository.save(existing);
    }

    @Override
    @Transactional
    public void deleteRule(Long ruleId) {
        if (ruleId == null) {
            throw new IllegalArgumentException("Rule ID cannot be null");
        }

        AlertRule rule = alertRuleRepository.findById(ruleId);
        if (rule == null) {
            throw new IllegalArgumentException("Alert rule with ID " + ruleId + " not found");
        }

        alertRuleRepository.delete(rule);
    }
}
