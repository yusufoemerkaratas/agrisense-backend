package io.agrisense.adapter.in.web.dto;

import io.agrisense.domain.model.ECondition;

public class AlertRuleResponse {
    private Long id;
    private Long sensorId;
    private String ruleName;
    private ECondition condition;
    private double threshold;
    private boolean isActive;

    public AlertRuleResponse(Long id, Long sensorId, String ruleName, ECondition condition, double threshold, boolean isActive) {
        this.id = id;
        this.sensorId = sensorId;
        this.ruleName = ruleName;
        this.condition = condition;
        this.threshold = threshold;
        this.isActive = isActive;
    }

    // Getters
    public Long getId() { return id; }
    public Long getSensorId() { return sensorId; }
    public String getRuleName() { return ruleName; }
    public ECondition getCondition() { return condition; }
    public double getThreshold() { return threshold; }
    public boolean isActive() { return isActive; }
}