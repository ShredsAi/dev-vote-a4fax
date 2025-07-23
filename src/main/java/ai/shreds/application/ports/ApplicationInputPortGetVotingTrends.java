package ai.shreds.application.ports;

import java.util.List;
import ai.shreds.shared.dtos.SharedVotingTrendItemDTO;

/**
 * Input port for retrieving voting trends over time.
 */
public interface ApplicationInputPortGetVotingTrends {

    /**
     * Retrieves voting trend data based on metric and time range.
     *
     * @param metric        Name of the trend metric (e.g., 'votes_per_hour').
     * @param timeInterval  Time interval for aggregation (ISO8601 duration string, optional).
     * @param fromTimestamp Start of the time range (ISO8601 string, optional).
     * @param toTimestamp   End of the time range (ISO8601 string, optional).
     * @return List of SharedVotingTrendItemDTO matching the criteria.
     */
    List<SharedVotingTrendItemDTO> getVotingTrends(
            String metric,
            String timeInterval,
            String fromTimestamp,
            String toTimestamp
    );
}
