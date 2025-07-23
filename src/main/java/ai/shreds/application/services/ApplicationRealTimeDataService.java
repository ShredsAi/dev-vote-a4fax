package ai.shreds.application.services;

import ai.shreds.application.ports.ApplicationInputPortStreamRealTimeData;
import ai.shreds.application.ports.ApplicationOutputPortMonitoring;
import ai.shreds.domain.services.DomainServiceRealTimeProcessing;
import ai.shreds.domain.ports.DomainOutputPortRealTimeDataRepository;
import ai.shreds.shared.dtos.SharedRealTimeDataDTO;
import ai.shreds.shared.dtos.SharedMonitoringEventDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import java.time.OffsetDateTime;
import java.util.HashMap;

/**
 * Service for streaming real-time metric data.
 */
@Service
@RequiredArgsConstructor
public class ApplicationRealTimeDataService implements ApplicationInputPortStreamRealTimeData {

    private final DomainServiceRealTimeProcessing domainServiceRealTimeProcessing;
    private final DomainOutputPortRealTimeDataRepository domainOutputPortRealTimeDataRepository;
    private final ApplicationOutputPortMonitoring applicationOutputPortMonitoring;

    @Override
    public Flux<SharedRealTimeDataDTO> streamRealTimeData(String metric) {
        // Start monitoring
        applicationOutputPortMonitoring.recordMetric(
            new SharedMonitoringEventDTO(
                "real_time_stream_started",
                metric,
                0,
                new HashMap<>() {{ put("metric", metric); }},
                OffsetDateTime.now().toString()
            )
        );
        return domainServiceRealTimeProcessing.streamMetricData(metric)
            .map(domainValue -> new SharedRealTimeDataDTO(
                metric,
                domainValue.getValue(),
                OffsetDateTime.now().toString()
            ))
            .doOnError(e -> applicationOutputPortMonitoring.recordError(
                "Error streaming real-time data",
                new HashMap<>() {{ put("metric", metric); put("error", e.getMessage()); }}
            ));
    }
}
