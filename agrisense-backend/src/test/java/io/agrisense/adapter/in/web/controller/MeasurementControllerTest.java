package io.agrisense.adapter.in.web.controller;

import io.agrisense.adapter.in.web.dto.CreateMeasurementRequest;
import io.agrisense.ports.in.ProcessMeasurementUseCase;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

public class MeasurementControllerTest {

    private ProcessMeasurementUseCase useCase;
    private MeasurementController controller;

    @BeforeEach
    public void setup() {
        useCase = Mockito.mock(ProcessMeasurementUseCase.class);
        controller = new MeasurementController(useCase);
    }

    // --- POST MEASUREMENT TESTS ---

    @Test
    public void testPostMeasurement_WithValidData_Returns202Accepted() {
        CreateMeasurementRequest req = new CreateMeasurementRequest();
        req.setSensorId(1L);
        req.setValue(23.5);
        req.setUnit("C");

        Response response = controller.postMeasurement(req);

        assertEquals(Response.Status.ACCEPTED.getStatusCode(), response.getStatus());
        assertNotNull(response.getEntity());
        assertTrue(response.getEntity().toString().contains("processed"));
    }

    @Test
    public void testPostMeasurement_WithMissingSensorId_Returns400() {
        CreateMeasurementRequest req = new CreateMeasurementRequest();
        req.setSensorId(null);
        req.setValue(23.5);
        req.setUnit("C");

        Response response = controller.postMeasurement(req);

        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }

    @Test
    public void testPostMeasurement_WithMissingValue_Returns400() {
        CreateMeasurementRequest req = new CreateMeasurementRequest();
        req.setSensorId(1L);
        req.setValue(null);
        req.setUnit("C");

        Response response = controller.postMeasurement(req);

        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }

    @Test
    public void testPostMeasurement_WithNullRequest_Returns400() {
        Response response = controller.postMeasurement(null);

        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }

    @Test
    public void testPostMeasurement_WithInvalidSensorId_Returns404() {
        CreateMeasurementRequest req = new CreateMeasurementRequest();
        req.setSensorId(999L);
        req.setValue(23.5);
        req.setUnit("C");

        Mockito.doThrow(new IllegalArgumentException("Sensor not found: 999"))
                .when(useCase).processMeasurement(999L, 23.5, "C");

        Response response = controller.postMeasurement(req);

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
    }

    @Test
    public void testPostMeasurement_WithGenericException_Returns500() {
        CreateMeasurementRequest req = new CreateMeasurementRequest();
        req.setSensorId(1L);
        req.setValue(23.5);
        req.setUnit("C");

        Mockito.doThrow(new RuntimeException("Unexpected error"))
                .when(useCase).processMeasurement(anyLong(), anyDouble(), anyString());

        Response response = controller.postMeasurement(req);

        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
    }

    @Test
    public void testPostMeasurement_DefaultUnit_HandlesMissing() {
        CreateMeasurementRequest req = new CreateMeasurementRequest();
        req.setSensorId(1L);
        req.setValue(25.0);
        req.setUnit(null);

        Response response = controller.postMeasurement(req);

        assertEquals(Response.Status.ACCEPTED.getStatusCode(), response.getStatus());
        // Verify that empty string is used as default unit
        Mockito.verify(useCase).processMeasurement(1L, 25.0, "");
    }

    @Test
    public void testPostMeasurement_WithEmptyUnit_AcceptsEmpty() {
        CreateMeasurementRequest req = new CreateMeasurementRequest();
        req.setSensorId(1L);
        req.setValue(30.0);
        req.setUnit("");

        Response response = controller.postMeasurement(req);

        assertEquals(Response.Status.ACCEPTED.getStatusCode(), response.getStatus());
        Mockito.verify(useCase).processMeasurement(1L, 30.0, "");
    }

    @Test
    public void testPostMeasurement_WithZeroValue_Accepted() {
        CreateMeasurementRequest req = new CreateMeasurementRequest();
        req.setSensorId(1L);
        req.setValue(0.0);
        req.setUnit("C");

        Response response = controller.postMeasurement(req);

        assertEquals(Response.Status.ACCEPTED.getStatusCode(), response.getStatus());
    }

    @Test
    public void testPostMeasurement_WithNegativeValue_Accepted() {
        CreateMeasurementRequest req = new CreateMeasurementRequest();
        req.setSensorId(1L);
        req.setValue(-5.0);
        req.setUnit("C");

        Response response = controller.postMeasurement(req);

        assertEquals(Response.Status.ACCEPTED.getStatusCode(), response.getStatus());
    }

    @Test
    public void testPostMeasurement_WithLargeValue_Accepted() {
        CreateMeasurementRequest req = new CreateMeasurementRequest();
        req.setSensorId(1L);
        req.setValue(999999.99);
        req.setUnit("ppm");

        Response response = controller.postMeasurement(req);

        assertEquals(Response.Status.ACCEPTED.getStatusCode(), response.getStatus());
    }
}
