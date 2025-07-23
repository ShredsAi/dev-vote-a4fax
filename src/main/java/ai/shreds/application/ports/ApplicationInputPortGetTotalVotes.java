package ai.shreds.application.ports;

import java.util.List;
import java.util.UUID;
import ai.shreds.shared.dtos.SharedTotalVotesItemDTO;

/**
 * Input port for retrieving total votes per candidate.
 */
public interface ApplicationInputPortGetTotalVotes {

    /**
     * Retrieves the total votes for a candidate, optionally filtered by polling station and time range.
     *
     * @param candidateId      UUID of the candidate (optional).
     * @param pollingStationId UUID of the polling station (optional).
     * @param fromTimestamp    Start of the time range (ISO8601 string, optional).
     * @param toTimestamp      End of the time range (ISO8601 string, optional).
     * @return List of SharedTotalVotesItemDTO matching the criteria.
     */
    List<SharedTotalVotesItemDTO> getTotalVotes(
            UUID candidateId,
            UUID pollingStationId,
            String fromTimestamp,
            String toTimestamp
    );
}
