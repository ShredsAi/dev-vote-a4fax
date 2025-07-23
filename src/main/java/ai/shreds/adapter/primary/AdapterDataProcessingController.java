package ai.shreds.adapter.primary;

import ai.shreds.application.ports.ApplicationInputPortProcessData;
import ai.shreds.shared.dtos.SharedProcessDataRequestDTO;
import ai.shreds.shared.dtos.SharedProcessDataResponseDTO;
import ai.shreds.adapter.exceptions.AdapterExceptionInvalidRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.OffsetDateTime;

/**
 * REST controller for processing batches of preprocessed voting data.
 */
@RestController
@RequestMapping("/api/aggregated")
@RequiredArgsConstructor
@Slf4j
public class AdapterDataProcessingController {

    private final ApplicationInputPortProcessData dataProcessingService;

    /**
     * Processes a batch of preprocessed voting data.
     */
    @PostMapping("/process-data")
    public ResponseEntity<SharedProcessDataResponseDTO> processData(
            @RequestBody SharedProcessDataRequestDTO request) {
        try {
            log.info("Processing data batch with {} records", 
                    request.getData() != null ? request.getData().size() : 0);
            
            if (request.getData() == null || request.getData().isEmpty()) {
                throw new AdapterExceptionInvalidRequestException(
                        "Data list cannot be null or empty", "EMPTY_DATA_LIST");
            }
            
            // Validate each data item
            for (int i = 0; i < request.getData().size(); i++) {
                var dataItem = request.getData().get(i);
                if (dataItem.getCandidateId() == null) {
                    throw new AdapterExceptionInvalidRequestException(
                            "Candidate ID is required for data item at index " + i, "MISSING_CANDIDATE_ID");
                }
                if (dataItem.getPollingStationId() == null) {
                    throw new AdapterExceptionInvalidRequestException(
                            "Polling station ID is required for data item at index " + i, "MISSING_STATION_ID");
                }
                if (dataItem.getVotes() == null || dataItem.getVotes() < 0) {
                    throw new AdapterExceptionInvalidRequestException(
                            "Valid vote count is required for data item at index " + i, "INVALID_VOTE_COUNT");
                }
            }
            
            dataProcessingService.processData(request.getData());
            
            SharedProcessDataResponseDTO response = new SharedProcessDataResponseDTO(
                    "success",
                    "Data processing initiated successfully",
                    OffsetDateTime.now().toString()
            );
            
            log.info("Successfully processed data batch with {} records", request.getData().size());
            return ResponseEntity.ok(response);
            
        } catch (AdapterExceptionInvalidRequestException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error processing data batch", e);
            throw new AdapterExceptionInvalidRequestException(
                    "Failed to process data: " + e.getMessage(), "DATA_PROCESSING_ERROR");
        }
    }
}