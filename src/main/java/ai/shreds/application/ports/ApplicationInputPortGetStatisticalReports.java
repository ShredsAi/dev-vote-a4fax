package ai.shreds.application.ports;

import java.util.List;
import ai.shreds.shared.dtos.SharedStatisticalReportItemDTO;

/**
 * Input port for retrieving statistical reports.
 */
public interface ApplicationInputPortGetStatisticalReports {

    /**
     * Retrieves statistical reports, optionally filtered by type and time range.
     *
     * @param reportType    Type of report (optional).
     * @param fromTimestamp Start of the time range (ISO8601 string, optional).
     * @param toTimestamp   End of the time range (ISO8601 string, optional).
     * @return List of SharedStatisticalReportItemDTO matching the criteria.
     */
    List<SharedStatisticalReportItemDTO> getStatisticalReports(
            String reportType,
            String fromTimestamp,
            String toTimestamp
    );
}
