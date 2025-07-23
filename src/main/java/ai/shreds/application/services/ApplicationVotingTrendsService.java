package ai.shreds.application.services;

import ai.shreds.application.ports.ApplicationInputPortGetVotingTrends;
import ai.shreds.application.ports.ApplicationOutputPortMonitoring;
import ai.shreds.domain.services.DomainServiceVotingTrendsAnalysis;
import ai.shreds.domain.ports.DomainOutputPortVotingTrendsOverTimeRepository;
import ai.shreds.domain.value_objects.DomainValueTimeInterval;
import ai.shreds.domain.entities.DomainEntityVotingTrendsOverTime;
import ai.shreds.shared.dtos.SharedVotingTrendItemDTO;
import ai.shreds.shared.dtos.SharedMonitoringEventDTO;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.HashMap;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApplicationVotingTrendsService implements ApplicationInputPortGetVotingTrends {

    private final DomainServiceVotingTrendsAnalysis domainServiceVotingTrendsAnalysis;
    private final DomainOutputPortVotingTrendsOverTimeRepository domainOutputPortVotingTrendsRepository;
    private final ApplicationOutputPortMonitoring applicationOutputPortMonitoring;

    @Override
    public List<SharedVotingTrendItemDTO> getVotingTrends(
            String metric,
            String timeInterval,
            String fromTimestamp,
            String toTimestamp
    ) {
        try {
            List<DomainEntityVotingTrendsOverTime> trends;
            
            // Query based on available parameters
            if (fromTimestamp != null && toTimestamp != null) {
                OffsetDateTime start = OffsetDateTime.parse(fromTimestamp, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
                OffsetDateTime end = OffsetDateTime.parse(toTimestamp, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
                
                if (metric != null && timeInterval != null) {
                    trends = domainOutputPortVotingTrendsRepository.findByMetricAndTimeInterval(metric, timeInterval)
                            .stream()
                            .filter(trend -> {
                                OffsetDateTime timestamp = trend.getTimestamp();
                                return (timestamp.isAfter(start) || timestamp.isEqual(start)) &&
                                       (timestamp.isBefore(end) || timestamp.isEqual(end));
                            })
                            .collect(Collectors.toList());
                } else {
                    trends = domainOutputPortVotingTrendsRepository.findByTimestampBetween(start, end);
                    if (metric != null) {
                        trends = trends.stream()
                                .filter(trend -> trend.getMetric().equals(metric))
                                .collect(Collectors.toList());
                    }
                }
            } else if (metric != null && timeInterval != null) {
                trends = domainOutputPortVotingTrendsRepository.findByMetricAndTimeInterval(metric, timeInterval);
            } else if (metric != null) {
                trends = domainOutputPortVotingTrendsRepository.findByMetric(metric);
            } else {
                // Analyze trends using domain service if no specific filters
                DomainValueTimeInterval timeIntervalValue = timeInterval != null ? 
                        new DomainValueTimeInterval(timeInterval) : 
                        new DomainValueTimeInterval("PT1H"); // Default to hourly
                trends = domainServiceVotingTrendsAnalysis.analyzeVotingTrends(
                        metric != null ? metric : "votes_per_hour", 
                        timeIntervalValue
                );
            }

            // Record metric for monitoring
            applicationOutputPortMonitoring.recordMetric(new SharedMonitoringEventDTO(
                "voting_trends_analyzed",
                metric != null ? metric : "general",
                (double) trends.size(),
                new HashMap<>() {{
                    put("timeInterval", timeInterval);
                    put("fromTimestamp", fromTimestamp);
                    put("toTimestamp", toTimestamp);
                }},
                OffsetDateTime.now().toString()
            ));

            return trends.stream()
                    .map(DomainEntityVotingTrendsOverTime::toDTO)
                    .toList();

        } catch (Exception e) {
            applicationOutputPortMonitoring.recordError(
                "Error analyzing voting trends",
                new HashMap<>() {{
                    put("metric", metric);
                    put("timeInterval", timeInterval);
                    put("fromTimestamp", fromTimestamp);
                    put("toTimestamp", toTimestamp);
                    put("error", e.getMessage());
                }}
            );
            throw e;
        }
    }
}
