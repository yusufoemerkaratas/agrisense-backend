package io.agrisense.ports.in;

import io.agrisense.domain.model.Sensor;
import java.util.List;

public interface ManageSensorUseCase {
    Sensor createSensor(Sensor sensor);
    List<Sensor> getAllSensors();
    Sensor getSensorById(Long id);
}