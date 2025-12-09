 package io.agrisense.adapter.in.web.dto;

import io.agrisense.domain.model.ECondition;


public class CreateAlertRuleRequest {
    private String name;
    private ECondition condition;
    private Double value;
    private String description;
    
    public CreateAlertRuleRequest() {
    }
    
    public CreateAlertRuleRequest(String name, ECondition condition, Double value, String description) {
        this.name = name;
        this.condition = condition;
        this.value = value;
        this.description = description;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public ECondition getCondition() {
        return condition;
    }
    
    public void setCondition(ECondition condition) {
        this.condition = condition;
    }
    
    public Double getValue() {
        return value;
    }
    
    public void setValue(Double value) {
        this.value = value;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
}