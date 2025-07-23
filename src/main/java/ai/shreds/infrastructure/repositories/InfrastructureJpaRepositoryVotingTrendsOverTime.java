package ai.shreds.infrastructure.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface InfrastructureJpaRepositoryVotingTrendsOverTime 
        extends JpaRepository<InfrastructureJpaEntityVotingTrendsOverTime, UUID> {

    List<InfrastructureJpaEntityVotingTrendsOverTime> findByMetric(String metric);
    
    List<InfrastructureJpaEntityVotingTrendsOverTime> findByMetricAndTimeInterval(String metric, String timeInterval);
    
    List<InfrastructureJpaEntityVotingTrendsOverTime> findByTimestampBetween(OffsetDateTime start, OffsetDateTime end);
}