package io.agrisense.adapter.in.web.controller;

import io.agrisense.adapter.in.web.dto.CreateMeasurementRequest;
import io.agrisense.ports.in.ProcessMeasurementUseCase;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/measurements")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MeasurementController {

    private final ProcessMeasurementUseCase processMeasurementUseCase;

    @Inject
    public MeasurementController(ProcessMeasurementUseCase processMeasurementUseCase) {
        this.processMeasurementUseCase = processMeasurementUseCase;
    }

    @POST
    public Response postMeasurement(CreateMeasurementRequest req) {
        // Validasyon
        if (req == null || req.getSensorId() == null || req.getValue() == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"sensorId and value are required\"}")
                    .build();
        }

        try {
            // Use Case Çağrısı (Hexagonal Mimariye Uygun)
            processMeasurementUseCase.processMeasurement(
                req.getSensorId(), 
                req.getValue(), 
                req.getUnit() == null ? "" : req.getUnit()
            );

            // Başarılı Dönüş (202 Accepted - İşlem sıraya alındı/yapıldı anlamında)
            return Response.accepted()
                    .entity("{\"status\": \"Measurement processed successfully\"}")
                    .build();

        } catch (IllegalArgumentException e) {
            // Sensör bulunamazsa vs.
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"Internal server error\"}")
                    .build();
        }
    }
}