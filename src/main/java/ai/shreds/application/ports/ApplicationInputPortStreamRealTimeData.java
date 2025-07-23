package ai.shreds.application.ports;

import reactor.core.publisher.Flux;
import ai.shreds.shared.dtos.SharedRealTimeDataDTO;

/**
 * Input port for streaming real-time data for a given metric.
 */
public interface ApplicationInputPortStreamRealTimeData {

    /**
     * Streams real-time data for the specified metric.
     *
     * @param metric name of the metric to stream
     * @return Flux of SharedRealTimeDataDTO
     */
    Flux<SharedRealTimeDataDTO> streamRealTimeData(String metric);
}
