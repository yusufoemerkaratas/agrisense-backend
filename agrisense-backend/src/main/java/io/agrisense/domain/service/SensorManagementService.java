package io.agrisense.domain.service;

import io.agrisense.domain.model.Sensor;
import io.agrisense.ports.in.ManageSensorUseCase;
import io.agrisense.ports.out.SensorRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class SensorManagementService implements ManageSensorUseCase {

    private final SensorRepository sensorRepository;

    public SensorManagementService(SensorRepository sensorRepository) {
        this.sensorRepository = sensorRepository;
    }

    @Override
    @Transactional
    public Sensor createSensor(Sensor sensor) {
        // İleride buraya "Aynı isimde sensör var mı?" kontrolü eklenebilir.
        return sensorRepository.save(sensor);
    }

    @Override
    public List<Sensor> getAllSensors() {
        return sensorRepository.findAll();
    }

    @Override
    public Sensor getSensorById(Long id) {
        Sensor sensor = sensorRepository.findById(id);
        if (sensor == null) {
            // İsteğe bağlı: Burada özel bir Domain Exception fırlatılabilir
             throw new IllegalArgumentException("Sensor with ID " + id + " not found.");
        }
        return sensor;
    }
}