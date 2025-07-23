package ai.shreds.application.services;

import ai.shreds.application.exceptions.ApplicationExceptionDataProcessingException;
import ai.shreds.application.ports.ApplicationInputPortAggregatePreprocessedData;
import ai.shreds.application.ports.ApplicationOutputPortMonitoring;
import ai.shreds.domain.services.DomainServiceDataAggregation;
import ai.shreds.domain.services.DomainServiceVotingTrendsAnalysis;
import ai.shreds.domain.services.DomainServiceTurnoutCalculation;
import ai.shreds.domain.services.DomainServiceMetricCalculation;
import ai.shreds.domain.value_objects.DomainValueTimeInterval;
import ai.shreds.domain.entities.DomainEntityVotingTrendsOverTime;
import ai.shreds.shared.dtos.SharedPreprocessedDataDTO;
import ai.shreds.shared.dtos.SharedMonitoringEventDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ApplicationDataAggregationService implements ApplicationInputPortAggregatePreprocessedData {

    private final DomainServiceDataAggregation domainServiceDataAggregation;
    private final DomainServiceVotingTrendsAnalysis domainServiceVotingTrendsAnalysis;
    private final DomainServiceTurnoutCalculation domainServiceTurnoutCalculation;
    private final DomainServiceMetricCalculation domainServiceMetricCalculation;
    private final ApplicationOutputPortMonitoring applicationOutputPortMonitoring;

    @Override
    public void aggregatePreprocessedData(SharedPreprocessedDataDTO data) {
        try {
            processTotalVotes(data);
            updateTurnoutRates(data);
            calculateTrends(data);
            updateMetrics(data);
        } catch (Exception e) {
            applicationOutputPortMonitoring.recordError(
                    "Error aggregating preprocessed data",
                    Map.of("data", data, "error", e.getMessage())
            );
            throw new ApplicationExceptionDataProcessingException(
                    "Data aggregation failed", "DATA_AGG_ERROR", Map.of("data", data, "cause", e.getMessage()), e
            );
        }
    }

    private void processTotalVotes(SharedPreprocessedDataDTO data) {
        domainServiceDataAggregation.aggregateTotalVotes(
                data.getCandidateId(), data.getCandidateName(), data.getVotes(), data.getPollingStationId()
        );
    }

    private void updateTurnoutRates(SharedPreprocessedDataDTO data) {
        domainServiceTurnoutCalculation.updateTurnoutRate(
                data.getPollingStationId(), data.getVotes()
        );
    }

    private void calculateTrends(SharedPreprocessedDataDTO data) {
        try {
            DomainValueTimeInterval interval = new DomainValueTimeInterval("PT1H");
            List<Double> values = List.of(data.getVotes().doubleValue());
            Double trendValue = domainServiceVotingTrendsAnalysis.calculateTrend(values, interval);
            
            // Use the factory method from domain service to create the trend entity properly
            DomainEntityVotingTrendsOverTime trendEntity = domainServiceVotingTrendsAnalysis.createTrendEntity(
                "votes_per_interval",
                interval,
                trendValue
            );
            
            domainServiceVotingTrendsAnalysis.saveTrend(trendEntity);
        } catch (Exception e) {
            applicationOutputPortMonitoring.recordError(
                "Error calculating voting trends",
                Map.of("candidateId", data.getCandidateId(), "error", e.getMessage())
            );
            throw new ApplicationExceptionDataProcessingException(
                "Trend calculation failed", "TREND_CALC_ERROR", Map.of("data", data, "cause", e.getMessage()), e
            );
        }
    }

    private void updateMetrics(SharedPreprocessedDataDTO data) {
        domainServiceMetricCalculation.updateMetric(
                "total_votes", data.getVotes().doubleValue()
        );
    }
}