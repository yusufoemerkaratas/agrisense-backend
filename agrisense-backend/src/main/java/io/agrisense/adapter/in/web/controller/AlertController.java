package io.agrisense.adapter.in.web.controller;

import io.agrisense.adapter.in.web.dto.AlertResponse;
import io.agrisense.adapter.in.web.dto.PagedResponse;
import io.agrisense.domain.model.Alert;
import io.agrisense.domain.model.EAlertStatus;
import io.agrisense.domain.model.PagedResult;
import io.agrisense.ports.in.IQueryAlertUseCase;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/alerts")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AlertController {

    @Inject
    IQueryAlertUseCase queryAlertUseCase;

    @GET
    public Response getAlerts(
            @QueryParam("status") String statusStr,
            @QueryParam("page") Integer page,
            @QueryParam("size") Integer size) {

        EAlertStatus status = null;
        if (statusStr != null && !statusStr.isEmpty()) {
            status = EAlertStatus.valueOf(statusStr.toUpperCase());
        }

        PagedResult<Alert> result = queryAlertUseCase.queryAlerts(status, page, size);

        var alerts = result.getContent().stream()
                .map(a -> {
                    AlertResponse resp = this.toResponse(a);
                    // Add HATEOAS links
                    resp.set_links(new io.agrisense.adapter.in.web.dto.HateoasLinks()
                            .addLink("self", "/api/alerts/" + a.getId())
                            .addLink("sensor", "/api/sensors/" + a.getSensorId())
                            .addLink("rule", "/api/alertRules/" + a.getRuleId()));
                    return resp;
                })
                .toList();

        PagedResponse<AlertResponse> response = new PagedResponse<>(
                alerts,
                result.getPage(),
                result.getSize(),
                result.getTotalElements(),
                result.getTotalPages()
        );

        return Response.ok(response).build();
    }

    private AlertResponse toResponse(Alert alert) {
        return new AlertResponse(
                alert.getId(),
                alert.getSensorId(),
                alert.getRuleId(),
                alert.getMessage(),
                alert.getStatus().name(),
                alert.getCreatedAt(),
                alert.getClosedAt()
        );
    }
}
