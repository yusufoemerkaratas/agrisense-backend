package io.agrisense.adapter.in.web.dto;

import io.agrisense.domain.model.ESensorType;

public class SensorResponse {
    private Long id;
    private String name;
    private ESensorType type;
    private String apiKey;
    private Long fieldId;

    public SensorResponse(Long id, String name, ESensorType type, String apiKey, Long fieldId) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.apiKey = apiKey;
        this.fieldId = fieldId;
    }

    // Getters only (Response olduğu için setter gerekmez)
    public Long getId() { return id; }
    public String getName() { return name; }
    public ESensorType getType() { return type; }
    public String getApiKey() { return apiKey; }
    public Long getFieldId() { return fieldId; }
}