package ai.shreds.adapter.primary;

import ai.shreds.application.ports.*;
import ai.shreds.shared.dtos.*;
import ai.shreds.shared.value_objects.*;
import ai.shreds.adapter.exceptions.AdapterExceptionInvalidRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.OffsetDateTime;
import java.util.List;

/**
 * REST controller for accessing aggregated data including total votes, trends, 
 * turnout rates, statistical reports, and aggregated metrics.
 */
@RestController
@RequestMapping("/api/aggregated")
@RequiredArgsConstructor
@Slf4j
public class AdapterAggregatedDataController {

    private final ApplicationInputPortGetTotalVotes totalVotesService;
    private final ApplicationInputPortGetVotingTrends votingTrendsService;
    private final ApplicationInputPortGetTurnoutRates turnoutRatesService;
    private final ApplicationInputPortGetStatisticalReports statisticalReportsService;
    private final ApplicationInputPortGetAggregatedMetrics aggregatedMetricsService;

    /**
     * Retrieves total votes per candidate.
     */
    @GetMapping("/total-votes")
    public ResponseEntity<SharedApiResponseDTO<List<SharedTotalVotesItemDTO>>> getTotalVotes(
            @ModelAttribute SharedTotalVotesRequestParams params) {
        try {
            log.info("Retrieving total votes with params: {}", params);
            
            List<SharedTotalVotesItemDTO> data = totalVotesService.getTotalVotes(
                    params.getCandidateId(),
                    params.getPollingStationId(),
                    params.getFromTimestamp(),
                    params.getToTimestamp()
            );
            
            SharedApiResponseDTO<List<SharedTotalVotesItemDTO>> response = new SharedApiResponseDTO<>(
                    "success",
                    data,
                    OffsetDateTime.now().toString()
            );
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error retrieving total votes", e);
            throw new AdapterExceptionInvalidRequestException("Failed to retrieve total votes: " + e.getMessage(), "TOTAL_VOTES_ERROR");
        }
    }

    /**
     * Retrieves voting trends over time.
     */
    @GetMapping("/voting-trends")
    public ResponseEntity<SharedApiResponseDTO<List<SharedVotingTrendItemDTO>>> getVotingTrends(
            @ModelAttribute SharedVotingTrendsRequestParams params) {
        try {
            log.info("Retrieving voting trends with params: {}", params);
            
            if (params.getMetric() == null || params.getMetric().trim().isEmpty()) {
                throw new AdapterExceptionInvalidRequestException("Metric parameter is required", "MISSING_METRIC");
            }
            
            List<SharedVotingTrendItemDTO> data = votingTrendsService.getVotingTrends(
                    params.getMetric(),
                    params.getTimeInterval(),
                    params.getFromTimestamp(),
                    params.getToTimestamp()
            );
            
            SharedApiResponseDTO<List<SharedVotingTrendItemDTO>> response = new SharedApiResponseDTO<>(
                    "success",
                    data,
                    OffsetDateTime.now().toString()
            );
            
            return ResponseEntity.ok(response);
        } catch (AdapterExceptionInvalidRequestException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error retrieving voting trends", e);
            throw new AdapterExceptionInvalidRequestException("Failed to retrieve voting trends: " + e.getMessage(), "VOTING_TRENDS_ERROR");
        }
    }

    /**
     * Retrieves turnout rates for polling stations.
     */
    @GetMapping("/turnout-rates")
    public ResponseEntity<SharedApiResponseDTO<List<SharedTurnoutRateItemDTO>>> getTurnoutRates(
            @ModelAttribute SharedTurnoutRatesRequestParams params) {
        try {
            log.info("Retrieving turnout rates with params: {}", params);
            
            List<SharedTurnoutRateItemDTO> data = turnoutRatesService.getTurnoutRates(
                    params.getStationId(),
                    params.getFromTimestamp(),
                    params.getToTimestamp()
            );
            
            SharedApiResponseDTO<List<SharedTurnoutRateItemDTO>> response = new SharedApiResponseDTO<>(
                    "success",
                    data,
                    OffsetDateTime.now().toString()
            );
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error retrieving turnout rates", e);
            throw new AdapterExceptionInvalidRequestException("Failed to retrieve turnout rates: " + e.getMessage(), "TURNOUT_RATES_ERROR");
        }
    }

    /**
     * Retrieves statistical reports.
     */
    @GetMapping("/statistical-reports")
    public ResponseEntity<SharedApiResponseDTO<List<SharedStatisticalReportItemDTO>>> getStatisticalReports(
            @ModelAttribute SharedStatisticalReportsRequestParams params) {
        try {
            log.info("Retrieving statistical reports with params: {}", params);
            
            List<SharedStatisticalReportItemDTO> data = statisticalReportsService.getStatisticalReports(
                    params.getReportType(),
                    params.getFromTimestamp(),
                    params.getToTimestamp()
            );
            
            SharedApiResponseDTO<List<SharedStatisticalReportItemDTO>> response = new SharedApiResponseDTO<>(
                    "success",
                    data,
                    OffsetDateTime.now().toString()
            );
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error retrieving statistical reports", e);
            throw new AdapterExceptionInvalidRequestException("Failed to retrieve statistical reports: " + e.getMessage(), "STATISTICAL_REPORTS_ERROR");
        }
    }

    /**
     * Retrieves aggregated metrics.
     */
    @GetMapping("/metrics")
    public ResponseEntity<SharedApiResponseDTO<List<SharedAggregatedMetricItemDTO>>> getAggregatedMetrics(
            @ModelAttribute SharedAggregatedMetricsRequestParams params) {
        try {
            log.info("Retrieving aggregated metrics with params: {}", params);
            
            List<SharedAggregatedMetricItemDTO> data = aggregatedMetricsService.getAggregatedMetrics(
                    params.getMetricName(),
                    params.getFromTimestamp(),
                    params.getToTimestamp()
            );
            
            SharedApiResponseDTO<List<SharedAggregatedMetricItemDTO>> response = new SharedApiResponseDTO<>(
                    "success",
                    data,
                    OffsetDateTime.now().toString()
            );
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error retrieving aggregated metrics", e);
            throw new AdapterExceptionInvalidRequestException("Failed to retrieve aggregated metrics: " + e.getMessage(), "AGGREGATED_METRICS_ERROR");
        }
    }
}