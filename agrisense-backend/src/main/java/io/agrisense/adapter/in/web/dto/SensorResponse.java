package io.agrisense.adapter.in.web.dto;

import io.agrisense.domain.model.ESensorType;

public class SensorResponse {
    private Long id;
    private String name;
    private ESensorType type;
    private String apiKey;
    private Long fieldId;
    private HateoasLinks _links;

    public SensorResponse(Long id, String name, ESensorType type, String apiKey, Long fieldId) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.apiKey = apiKey;
        this.fieldId = fieldId;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public ESensorType getType() { return type; }
    public String getApiKey() { return apiKey; }
    public Long getFieldId() { return fieldId; }
    public HateoasLinks get_links() { return _links; }
    public void set_links(HateoasLinks _links) { this._links = _links; }
}