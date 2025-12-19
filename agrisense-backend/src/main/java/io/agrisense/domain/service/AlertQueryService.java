package io.agrisense.domain.service;

import io.agrisense.domain.model.Alert;
import io.agrisense.domain.model.EAlertStatus;
import io.agrisense.domain.model.PagedResult;
import io.agrisense.ports.in.IQueryAlertUseCase;
import io.agrisense.ports.out.IAlertRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class AlertQueryService implements IQueryAlertUseCase {

    @Inject
    IAlertRepository alertRepository;

    @Override
    public PagedResult<Alert> queryAlerts(EAlertStatus status, Integer page, Integer size) {
        // Validate and set defaults
        if (page == null || page < 1) {
            page = 1;
        }
        if (size == null || size < 1 || size > 100) {
            size = 50;
        }

        int offset = (page - 1) * size;

        java.util.List<Alert> alerts = alertRepository.findByStatus(status, offset, size);
        long totalElements = alertRepository.countByStatus(status);

        return new PagedResult<>(alerts, page, size, totalElements);
    }
}
