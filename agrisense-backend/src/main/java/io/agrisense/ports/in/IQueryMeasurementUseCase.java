package io.agrisense.ports.in;

import io.agrisense.domain.model.Measurement;
import io.agrisense.domain.model.PagedResult;
import java.time.Instant;

/**
 * UC-02: Filter & List Measurements
 * Port for querying measurements with filtering and pagination
 */
public interface IQueryMeasurementUseCase {
    
    /**
     * Query measurements with filters and pagination
     * 
     * @param fieldId Filter by field (optional)
     * @param from Start date filter (optional)
     * @param to End date filter (optional)
     * @param page Page number (1-based)
     * @param size Page size
     * @return Paged result of measurements
     */
    PagedResult<Measurement> queryMeasurements(
        Long fieldId, 
        Instant from, 
        Instant to, 
        int page, 
        int size
    );
}
