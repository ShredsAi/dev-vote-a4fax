package ai.shreds.application.services;

import ai.shreds.application.exceptions.ApplicationExceptionAuthenticationException;
import ai.shreds.application.exceptions.ApplicationExceptionValidationException;
import ai.shreds.application.exceptions.ApplicationExceptionDataProcessingException;
import ai.shreds.application.ports.ApplicationInputPortProcessData;
import ai.shreds.application.ports.ApplicationInputPortAggregatePreprocessedData;
import ai.shreds.application.ports.ApplicationOutputPortAuthentication;
import ai.shreds.application.ports.ApplicationOutputPortMonitoring;
import ai.shreds.shared.dtos.SharedPreprocessedDataDTO;
import ai.shreds.shared.dtos.SharedAuthenticationDTO;
import ai.shreds.shared.dtos.SharedMonitoringEventDTO;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

/**
 * Service for processing preprocessed data requests.
 */
@Service
@RequiredArgsConstructor
public class ApplicationDataProcessingService implements ApplicationInputPortProcessData {

    private final ApplicationInputPortAggregatePreprocessedData applicationDataAggregationService;
    private final ApplicationOutputPortAuthentication applicationOutputPortAuthentication;
    private final ApplicationOutputPortMonitoring applicationOutputPortMonitoring;

    @Override
    public void processData(List<SharedPreprocessedDataDTO> data) {
        try {
            validateAndAuthorize(data);
            data.forEach(applicationDataAggregationService::aggregatePreprocessedData);
            applicationOutputPortMonitoring.recordMetric(new SharedMonitoringEventDTO(
                "data_processing_completed",
                "processing",
                data.size(),
                Map.of(),
                OffsetDateTime.now().toString()
            ));
        } catch (ApplicationExceptionAuthenticationException | ApplicationExceptionValidationException e) {
            applicationOutputPortMonitoring.recordError("Authentication/Validation failure", Map.of("error", e.getMessage()));
            throw e;
        } catch (Exception e) {
            applicationOutputPortMonitoring.recordError("Error processing data", Map.of("error", e.getMessage()));
            throw new ApplicationExceptionDataProcessingException(
                "Data processing failed", "PROCESS_DATA_ERROR", Map.of("error", e.getMessage()), e
            );
        }
    }

    private void validateAndAuthorize(List<SharedPreprocessedDataDTO> data) {
        // Authenticate using system credentials
        SharedAuthenticationDTO authRequest = new SharedAuthenticationDTO(
            "system", "", List.of(), OffsetDateTime.now().toString()
        );
        boolean authenticated = applicationOutputPortAuthentication.authenticate(authRequest);
        if (!authenticated) {
            throw new ApplicationExceptionAuthenticationException(
                "Authentication failed", "AUTH_FAIL", authRequest.getUserId()
            );
        }
        // Validate input data
        if (data == null || data.isEmpty()) {
            throw new ApplicationExceptionValidationException(
                "No data provided for processing", "VALIDATION_EMPTY_DATA", List.of("data list cannot be empty")
            );
        }
    }
}
