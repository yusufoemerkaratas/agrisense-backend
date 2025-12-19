package io.agrisense.domain.service;

import io.agrisense.domain.model.Sensor;
import io.agrisense.ports.in.IManageSensorUseCase;
import io.agrisense.ports.out.ISensorRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import io.quarkus.cache.CacheInvalidate;
import io.quarkus.cache.CacheResult;

import java.util.List;

@ApplicationScoped
public class SensorManagementService implements IManageSensorUseCase {

    private final ISensorRepository sensorRepository;

    public SensorManagementService(ISensorRepository sensorRepository) {
        this.sensorRepository = sensorRepository;
    }

    @Override
    @Transactional
    @CacheInvalidate(cacheName = "sensors-cache")
    public Sensor createSensor(Sensor sensor) {
        // İleride buraya "Aynı isimde sensör var mı?" kontrolü eklenebilir.
        return sensorRepository.save(sensor);
    }

    @Override
    @CacheResult(cacheName = "sensors-cache")
    public List<Sensor> getAllSensors() {
        return sensorRepository.findAll();
    }

    @Override
    @CacheResult(cacheName = "sensor-by-id")
    public Sensor getSensorById(Long id) {
        Sensor sensor = sensorRepository.findById(id);
        if (sensor == null) {
            // İsteğe bağlı: Burada özel bir Domain Exception fırlatılabilir
             throw new IllegalArgumentException("Sensor with ID " + id + " not found.");
        }
        return sensor;
    }

    @Override
    @Transactional
    @CacheInvalidate(cacheName = "sensors-cache")
    public Sensor updateSensor(Long id, Sensor updatedSensor) {
        Sensor existing = sensorRepository.findById(id);
        if (existing == null) {
            throw new IllegalArgumentException("Sensor with ID " + id + " not found.");
        }
        // Update fields
        existing.setName(updatedSensor.getName());
        existing.setType(updatedSensor.getType());
        existing.setApiKey(updatedSensor.getApiKey());
        return sensorRepository.save(existing);
    }

    @Override
    @Transactional
    @CacheInvalidate(cacheName = "sensors-cache")
    public void deleteSensor(Long id) {
        Sensor sensor = sensorRepository.findById(id);
        if (sensor == null) {
            throw new IllegalArgumentException("Sensor with ID " + id + " not found.");
        }
        sensorRepository.delete(sensor);
    }
}