package ai.shreds.application.ports;

import java.util.List;
import java.util.UUID;
import ai.shreds.shared.dtos.SharedTurnoutRateItemDTO;

/**
 * Input port for retrieving turnout rates for polling stations.
 */
public interface ApplicationInputPortGetTurnoutRates {

    /**
     * Retrieves turnout rates, optionally filtered by station and time range.
     *
     * @param stationId     UUID of the polling station (optional).
     * @param fromTimestamp Start of the time range (ISO8601 string, optional).
     * @param toTimestamp   End of the time range (ISO8601 string, optional).
     * @return List of SharedTurnoutRateItemDTO matching the criteria.
     */
    List<SharedTurnoutRateItemDTO> getTurnoutRates(
            UUID stationId,
            String fromTimestamp,
            String toTimestamp
    );
}
