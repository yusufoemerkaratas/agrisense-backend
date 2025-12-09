package io.agrisense.adapter.in.web.controller;

import io.agrisense.adapter.in.web.dto.CreateSensorRequest;
import io.agrisense.adapter.in.web.dto.SensorResponse;
import io.agrisense.adapter.in.web.mapper.SensorWebMapper;
import io.agrisense.domain.model.Sensor;
import io.agrisense.ports.in.ManageSensorUseCase;

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

    private final ManageSensorUseCase manageSensorUseCase;
    private final SensorWebMapper sensorMapper;

    @Inject
    public SensorController(ManageSensorUseCase manageSensorUseCase, SensorWebMapper sensorMapper) {
        this.manageSensorUseCase = manageSensorUseCase;
        this.sensorMapper = sensorMapper;
    }

    @POST
    public Response createSensor(@Valid CreateSensorRequest req) {
        // Basit validasyon
        
        // 1. DTO -> Domain
        Sensor sensorDomain = sensorMapper.toDomain(req);
        
        // 2. Use Case Call (Artık Repository değil!)
        Sensor savedSensor = manageSensorUseCase.createSensor(sensorDomain);
        
        // 3. Domain -> DTO
        SensorResponse response = sensorMapper.toResponse(savedSensor);

        return Response.status(Response.Status.CREATED).entity(response).build();
    }

    @GET
    public Response getAllSensors() {
        List<Sensor> sensors = manageSensorUseCase.getAllSensors();
        List<SensorResponse> responseList = sensorMapper.toResponseList(sensors);
        return Response.ok(responseList).build();
    }

    @GET
    @Path("/{id}")
    public Response getSensorById( @PathParam("id") Long id) {
        if (id == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("{\"error\": \"id is required\"}").build();
        }
        
        Sensor sensor = manageSensorUseCase.getSensorById(id);
        
        if (sensor == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("{\"error\": \"sensor not found\"}").build();
        }
        
        return Response.ok(sensorMapper.toResponse(sensor)).build();
    }
}