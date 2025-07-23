package ai.shreds.application.ports;

import ai.shreds.shared.dtos.SharedMonitoringEventDTO;
import java.util.Map;

/**
 * Output port for recording metrics and errors for monitoring purposes.
 */
public interface ApplicationOutputPortMonitoring {

    /**
     * Records a monitoring event metric.
     *
     * @param event monitoring event data
     */
    void recordMetric(SharedMonitoringEventDTO event);

    /**
     * Records an error with context for monitoring.
     *
     * @param error   error message
     * @param context context map of additional data
     */
    void recordError(String error, Map<String, Object> context);
}
