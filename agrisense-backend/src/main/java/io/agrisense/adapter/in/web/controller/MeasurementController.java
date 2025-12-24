package io.agrisense.adapter.in.web.controller;

import io.agrisense.adapter.in.web.dto.CreateMeasurementRequest;
import io.agrisense.adapter.in.web.dto.MeasurementResponse;
import io.agrisense.adapter.in.web.dto.PagedResponse;
import io.agrisense.domain.model.Measurement;
import io.agrisense.domain.model.PagedResult;
import io.agrisense.ports.in.IProcessMeasurementUseCase;
import io.agrisense.ports.in.IQueryMeasurementUseCase;
import jakarta.inject.Inject;

import jakarta.ws.rs.*;

import jakarta.validation.Valid;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Path("/api/measurements")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MeasurementController {

    private final IProcessMeasurementUseCase processMeasurementUseCase;
    private final IQueryMeasurementUseCase queryMeasurementUseCase;

    @Inject
    public MeasurementController(
            IProcessMeasurementUseCase processMeasurementUseCase,
            IQueryMeasurementUseCase queryMeasurementUseCase) {
        this.processMeasurementUseCase = processMeasurementUseCase;
        this.queryMeasurementUseCase = queryMeasurementUseCase;
    }

    @POST
    public Response postMeasurement(@Valid CreateMeasurementRequest req) {
        // Bean Validation (@NotNull, @Positive) handles field validation
        // GlobalExceptionHandler catches ConstraintViolationException → 400 Bad Request
        // GlobalExceptionHandler catches IllegalArgumentException → 404 Not Found
        
        // Use Case Call (Hexagonal Architecture Pattern)
        processMeasurementUseCase.processMeasurement(
            req.getSensorId(), 
            req.getValue(), 
            req.getUnit() == null ? "" : req.getUnit()
        );

        // 202 Accepted - Processing initiated asynchronously
        return Response.accepted()
                .entity("{\"status\": \"Measurement processed successfully\"}")
                .build();
    }

    /**
     * UC-02: Filter & List Measurements
     * GET /api/measurements?fieldId=10&from=...&to=...&page=1&size=50
     */
    @GET
    public Response getMeasurements(
            @QueryParam("fieldId") Long fieldId,
            @QueryParam("from") String fromStr,
            @QueryParam("to") String toStr,
            @QueryParam("page") @DefaultValue("1") int page,
            @QueryParam("size") @DefaultValue("50") int size) {
        
        // Parse dates
        Instant from = fromStr != null ? Instant.parse(fromStr) : null;
        Instant to = toStr != null ? Instant.parse(toStr) : null;

        // Query measurements
        PagedResult<Measurement> result = queryMeasurementUseCase.queryMeasurements(
            fieldId, from, to, page, size
        );

        // Convert to response DTO
        List<MeasurementResponse> responseList = result.getContent().stream()
            .map(m -> {
                MeasurementResponse resp = new MeasurementResponse(
                    m.getId(),
                    m.getSensorId(),
                    m.getTimestamp(),
                    m.getValue(),
                    m.getUnit()
                );
                // Add HATEOAS links
                resp.set_links(new io.agrisense.adapter.in.web.dto.HateoasLinks()
                        .addLink("self", "/api/measurements/" + m.getId())
                        .addLink("sensor", "/api/sensors/" + m.getSensorId()));
                return resp;
            })
            .collect(Collectors.toList());

        PagedResponse<MeasurementResponse> response = new PagedResponse<>(
            responseList,
            result.getPage(),
            result.getSize(),
            result.getTotalElements(),
            result.getTotalPages()
        );

        return Response.ok(response).build();
    }
}