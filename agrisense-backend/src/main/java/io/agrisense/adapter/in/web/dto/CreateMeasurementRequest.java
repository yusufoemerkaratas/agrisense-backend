package io.agrisense.adapter.in.web.dto;

public class CreateMeasurementRequest {
    private Long sensorId;
    private Double value;
    private String unit;

    public CreateMeasurementRequest() { }

    public Long getSensorId() { return sensorId; }
    public void setSensorId(Long sensorId) { this.sensorId = sensorId; }
    
    public Double getValue() { return value; }
    public void setValue(Double value) { this.value = value; }
    
    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
}