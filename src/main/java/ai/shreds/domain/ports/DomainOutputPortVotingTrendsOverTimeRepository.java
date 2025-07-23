package ai.shreds.domain.ports;

import ai.shreds.domain.entities.DomainEntityVotingTrendsOverTime;
import java.time.OffsetDateTime;
import java.util.List;

public interface DomainOutputPortVotingTrendsOverTimeRepository {
    DomainEntityVotingTrendsOverTime save(DomainEntityVotingTrendsOverTime entity);
    
    List<DomainEntityVotingTrendsOverTime> findByMetric(String metric);
    
    List<DomainEntityVotingTrendsOverTime> findByMetricAndTimeInterval(String metric, String timeInterval);
    
    List<DomainEntityVotingTrendsOverTime> findByTimestampBetween(OffsetDateTime start, OffsetDateTime end);
}