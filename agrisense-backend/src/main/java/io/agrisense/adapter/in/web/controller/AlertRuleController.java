package io.agrisense.adapter.in.web.controller;

import java.net.URI;
import java.util.List;

import io.agrisense.adapter.in.web.dto.CreateAlertRuleRequest;
import io.agrisense.adapter.in.web.dto.AlertRuleResponse;
import io.agrisense.adapter.in.web.mapper.AlertRuleWebMapper;
import io.agrisense.domain.model.AlertRule;
import io.agrisense.ports.in.ManageAlertRuleUseCase;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/sensors/{sensorId}/rules")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AlertRuleController {
    
    private final ManageAlertRuleUseCase alertRuleUseCase;
    private final AlertRuleWebMapper alertRuleMapper;
    
    @Inject
    public AlertRuleController(ManageAlertRuleUseCase alertRuleUseCase, AlertRuleWebMapper alertRuleMapper) {
        this.alertRuleUseCase = alertRuleUseCase;
        this.alertRuleMapper = alertRuleMapper;
    }
    
    @POST
    public Response createAlertRule(
            @PathParam("sensorId") Long sensorId,
            CreateAlertRuleRequest request) {
        
        try {
            // 1. DTO -> Domain Çevirimi (Mapper ile)
            AlertRule ruleDomain = alertRuleMapper.toDomain(request);

            // 2. Servis Çağrısı (Domain nesnesi ile)
            AlertRule createdRule = alertRuleUseCase.createRule(sensorId, ruleDomain);

            // 3. Domain -> DTO Çevirimi (Response için)
            AlertRuleResponse responseDTO = alertRuleMapper.toResponse(createdRule);

            return Response
                    .created(URI.create("/sensors/" + sensorId + "/rules/" + createdRule.getId()))
                    .entity(responseDTO)
                    .build();

        } catch (IllegalArgumentException e) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"Internal server error\"}")
                    .build();
        }
    }
    
    @GET
    public Response getActiveAlertRules(@PathParam("sensorId") Long sensorId) {
        try {
            // Servisten gelen Domain listesi
            List<AlertRule> activeRules = alertRuleUseCase.getActiveRules(sensorId);
            
            // Domain listesini Response DTO listesine çevir
            List<AlertRuleResponse> responseList = alertRuleMapper.toResponseList(activeRules);
            
            return Response.ok(responseList).build();
            
        } catch (IllegalArgumentException e) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"Internal server error\"}")
                    .build();
        }
    }
}