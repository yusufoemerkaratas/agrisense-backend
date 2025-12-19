package io.agrisense.adapter.in.web.controller;

import io.agrisense.adapter.in.web.dto.CreateSensorRequest;
import io.agrisense.adapter.in.web.dto.SensorResponse;
import io.agrisense.adapter.in.web.mapper.SensorWebMapper;
import io.agrisense.domain.model.Sensor;
import io.agrisense.ports.in.IManageSensorUseCase;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/api/sensors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorController {

    private final IManageSensorUseCase manageSensorUseCase;
    private final SensorWebMapper sensorMapper;

    @Inject
    public SensorController(IManageSensorUseCase manageSensorUseCase, SensorWebMapper sensorMapper) {
        this.manageSensorUseCase = manageSensorUseCase;
        this.sensorMapper = sensorMapper;
    }

    @POST
    public Response createSensor(@Valid CreateSensorRequest req) {
        // Basit validasyon (el yazısı hissiyle zorunlu alan kontrolü)
        if (req == null || req.getName() == null || req.getType() == null || req.getApiKey() == null || req.getFieldId() == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"name, type, apiKey, fieldId are required\"}")
                    .build();
        }

        // 1. DTO -> Domain
        Sensor sensorDomain = sensorMapper.toDomain(req);
        if (sensorDomain == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"invalid sensor payload\"}")
                    .build();
        }
        
        // 2. Use Case Call
        Sensor savedSensor = manageSensorUseCase.createSensor(sensorDomain);
        
        // 3. Domain -> DTO
        SensorResponse response = sensorMapper.toResponse(savedSensor);
        // Add HATEOAS links
        response.set_links(new io.agrisense.adapter.in.web.dto.HateoasLinks()
                .addLink("self", "/api/sensors/" + savedSensor.getId())
                .addLink("all", "/api/sensors"));

        return Response.status(Response.Status.CREATED).entity(response).build();
    }

    @GET
    public Response getAllSensors() {
        List<Sensor> sensors = manageSensorUseCase.getAllSensors();
        List<SensorResponse> responseList = sensorMapper.toResponseList(sensors);
        // Add HATEOAS links to each sensor
        for (SensorResponse resp : responseList) {
            resp.set_links(new io.agrisense.adapter.in.web.dto.HateoasLinks()
                    .addLink("self", "/api/sensors/" + resp.getId())
                    .addLink("all", "/api/sensors"));
        }
        return Response.ok(responseList).build();
    }

    @GET
    @Path("/{id}")
    public Response getSensorById( @PathParam("id") Long id) {
        if (id == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("{\"error\": \"id is required\"}").build();
        }
        
        try {
            Sensor sensor = manageSensorUseCase.getSensorById(id);
            
            SensorResponse response = sensorMapper.toResponse(sensor);
            // Add HATEOAS links
            response.set_links(new io.agrisense.adapter.in.web.dto.HateoasLinks()
                    .addLink("self", "/api/sensors/" + sensor.getId())
                    .addLink("all", "/api/sensors"));
            
            return Response.ok(response).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response updateSensor(@PathParam("id") Long id, @Valid CreateSensorRequest req) {
        if (id == null || req == null || req.getName() == null || req.getType() == null || req.getApiKey() == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"id, name, type, apiKey are required\"}")
                    .build();
        }

        try {
            Sensor sensorDomain = sensorMapper.toDomain(req);
            Sensor updated = manageSensorUseCase.updateSensor(id, sensorDomain);
            SensorResponse response = sensorMapper.toResponse(updated);
            response.set_links(new io.agrisense.adapter.in.web.dto.HateoasLinks()
                    .addLink("self", "/api/sensors/" + updated.getId())
                    .addLink("all", "/api/sensors"));
            return Response.ok(response).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteSensor(@PathParam("id") Long id) {
        if (id == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"id is required\"}")
                    .build();
        }

        try {
            manageSensorUseCase.deleteSensor(id);
            return Response.noContent().build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }
}