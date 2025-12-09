package io.agrisense.adapter.in.web.dto;

import io.agrisense.domain.model.ESensorType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class CreateSensorRequest {

    // 1. @NotBlank: Used for Strings. Cannot be null and cannot be empty ("").
    @NotBlank(message = "Sensor name cannot be empty")
    @Size(min = 3, max = 50, message = "Sensor name must be between 3 and 50 characters")
    private String name;

    // 2. @NotNull: Used for Enums, Numbers, or Objects.
    @NotNull(message = "Sensor type is required")
    private ESensorType type;

    // 3. API Key is usually mandatory.
    @NotBlank(message = "API Key is required")
    private String apiKey;

    // 4. IDs cannot be null and must be positive numbers.
    @NotNull(message = "Field ID is required")
    @Positive(message = "Field ID must be a positive number")
    private Long fieldId;

    // --- Getters and Setters ---
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public ESensorType getType() { return type; }
    public void setType(ESensorType type) { this.type = type; }

    public String getApiKey() { return apiKey; }
    public void setApiKey(String apiKey) { this.apiKey = apiKey; }

    public Long getFieldId() { return fieldId; }
    public void setFieldId(Long fieldId) { this.fieldId = fieldId; }
}