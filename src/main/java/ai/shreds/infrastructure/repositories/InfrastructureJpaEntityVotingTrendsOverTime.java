package ai.shreds.infrastructure.repositories;

import jakarta.persistence.*;
import lombok.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "voting_trends_over_time")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InfrastructureJpaEntityVotingTrendsOverTime {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "trend_id", nullable = false)
    private UUID trendId;

    @Column(name = "metric", nullable = false)
    private String metric;

    @Column(name = "time_interval", nullable = false)
    private String timeInterval;

    @Column(name = "value", nullable = false)
    private Double value;

    @Column(name = "timestamp", nullable = false)
    private OffsetDateTime timestamp;
}