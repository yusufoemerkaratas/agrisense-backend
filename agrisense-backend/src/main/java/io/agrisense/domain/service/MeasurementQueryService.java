package io.agrisense.domain.service;

import io.agrisense.domain.model.Measurement;
import io.agrisense.domain.model.PagedResult;
import io.agrisense.ports.in.IQueryMeasurementUseCase;
import io.agrisense.ports.out.IMeasurementRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.Instant;

/**
 * UC-02: Filter & List Measurements
 * Service for querying measurements with filtering and pagination
 */
@ApplicationScoped
public class MeasurementQueryService implements IQueryMeasurementUseCase {

    private final IMeasurementRepository measurementRepository;

    @Inject
    public MeasurementQueryService(IMeasurementRepository measurementRepository) {
        this.measurementRepository = measurementRepository;
    }

    @Override
    public PagedResult<Measurement> queryMeasurements(Long fieldId, Instant from, Instant to, int page, int size) {
        // Validate pagination parameters
        if (page < 1) {
            page = 1;
        }
        if (size < 1 || size > 100) {
            size = 50; // Default size
        }

        return measurementRepository.findByFilters(fieldId, from, to, page, size);
    }
}
