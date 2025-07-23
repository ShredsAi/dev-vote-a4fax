package ai.shreds.domain.services;

import ai.shreds.domain.entities.DomainEntityVotingTrendsOverTime;
import ai.shreds.domain.exceptions.DomainExceptionDataValidationException;
import ai.shreds.domain.ports.DomainOutputPortVotingTrendsOverTimeRepository;
import ai.shreds.domain.value_objects.DomainValueTimeInterval;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public class DomainServiceVotingTrendsAnalysis {
    private final DomainOutputPortVotingTrendsOverTimeRepository votingTrendsRepository;

    public DomainServiceVotingTrendsAnalysis(DomainOutputPortVotingTrendsOverTimeRepository votingTrendsRepository) {
        this.votingTrendsRepository = votingTrendsRepository;
    }

    public List<DomainEntityVotingTrendsOverTime> analyzeVotingTrends(String metric, DomainValueTimeInterval timeInterval) {
        return votingTrendsRepository.findByMetricAndTimeInterval(metric, timeInterval.toString());
    }

    public Double calculateTrend(List<Double> dataPoints, DomainValueTimeInterval timeInterval) {
        if (dataPoints == null || dataPoints.isEmpty()) {
            return 0.0;
        }
        
        if (dataPoints.size() == 1) {
            return dataPoints.get(0);
        }
        
        // Simple linear trend calculation using least squares method
        int n = dataPoints.size();
        double sumX = 0, sumY = 0, sumXY = 0, sumX2 = 0;
        
        for (int i = 0; i < n; i++) {
            sumX += i;
            sumY += dataPoints.get(i);
            sumXY += i * dataPoints.get(i);
            sumX2 += i * i;
        }
        
        // Calculate slope (trend)
        double slope = (n * sumXY - sumX * sumY) / (n * sumX2 - sumX * sumX);
        return slope;
    }

    public DomainEntityVotingTrendsOverTime saveTrend(DomainEntityVotingTrendsOverTime trend) {
        return votingTrendsRepository.save(trend);
    }
    
    public DomainEntityVotingTrendsOverTime createTrendEntity(String metric, DomainValueTimeInterval timeInterval, 
                                                             Double trendValue) 
            throws DomainExceptionDataValidationException {
        return new DomainEntityVotingTrendsOverTime(
            UUID.randomUUID(),
            metric,
            timeInterval,
            trendValue,
            OffsetDateTime.now()
        );
    }
}