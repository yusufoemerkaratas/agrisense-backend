package io.agrisense.adapter.in.web.controller;

import java.net.URI;
import java.util.List;

import io.agrisense.adapter.in.web.dto.CreateAlertRuleRequest;
import io.agrisense.adapter.in.web.dto.AlertRuleResponse;
import io.agrisense.adapter.in.web.mapper.AlertRuleWebMapper;
import io.agrisense.domain.model.AlertRule;
import io.agrisense.ports.in.IManageAlertRuleUseCase;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/sensors/{sensorId}/rules")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AlertRuleController {
    
    private final IManageAlertRuleUseCase alertRuleUseCase;
    private final AlertRuleWebMapper alertRuleMapper;
    
    @Inject
    public AlertRuleController(IManageAlertRuleUseCase alertRuleUseCase, AlertRuleWebMapper alertRuleMapper) {
        this.alertRuleUseCase = alertRuleUseCase;
        this.alertRuleMapper = alertRuleMapper;
    }
    
    @POST
    public Response createAlertRule(
            @PathParam("sensorId") Long sensorId,
            CreateAlertRuleRequest request) {
        
        try {
            // Basic validation
            if (sensorId == null || request == null || request.getName() == null || request.getCondition() == null || request.getThreshold() == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"error\": \"sensorId, name, condition, threshold are required\"}")
                        .build();
            }
            // 1. DTO -> Domain Çevirimi (Mapper ile)
            AlertRule ruleDomain = alertRuleMapper.toDomain(request);

            if (ruleDomain == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"error\": \"Invalid alert rule data\"}")
                        .build();
            }

            // 2. Servis Çağrısı (Domain nesnesi ile)
            AlertRule createdRule = alertRuleUseCase.createRule(sensorId, ruleDomain);

            if (createdRule == null || createdRule.getId() == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"error\": \"Unable to create alert rule\"}")
                        .build();
            }

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

    @PUT
    @Path("/{ruleId}")
    public Response updateAlertRule(
            @PathParam("sensorId") Long sensorId,
            @PathParam("ruleId") Long ruleId,
            CreateAlertRuleRequest request) {
        
        try {
            AlertRule ruleDomain = alertRuleMapper.toDomain(request);
            AlertRule updated = alertRuleUseCase.updateRule(ruleId, ruleDomain);
            AlertRuleResponse responseDTO = alertRuleMapper.toResponse(updated);
            return Response.ok(responseDTO).build();
        } catch (IllegalArgumentException e) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }

    @DELETE
    @Path("/{ruleId}")
    public Response deleteAlertRule(
            @PathParam("sensorId") Long sensorId,
            @PathParam("ruleId") Long ruleId) {
        
        try {
            alertRuleUseCase.deleteRule(ruleId);
            return Response.noContent().build();
        } catch (IllegalArgumentException e) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }
}