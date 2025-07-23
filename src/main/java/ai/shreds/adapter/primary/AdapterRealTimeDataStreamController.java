package ai.shreds.adapter.primary;

import ai.shreds.application.ports.ApplicationInputPortStreamRealTimeData;
import ai.shreds.shared.dtos.SharedRealTimeDataDTO;
import ai.shreds.shared.value_objects.SharedRealTimeFeedRequestParams;
import ai.shreds.adapter.exceptions.AdapterExceptionStreamingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

/**
 * Controller for streaming real-time data feeds.
 */
@RestController
@RequestMapping("/api/aggregated")
@RequiredArgsConstructor
@Slf4j
public class AdapterRealTimeDataStreamController {

    private final ApplicationInputPortStreamRealTimeData realTimeDataService;

    /**
     * Streams real-time data for a specified metric using Server-Sent Events (SSE).
     */
    @GetMapping(value = "/real-time-feed", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<SharedRealTimeDataDTO> streamRealTimeData(
            @ModelAttribute SharedRealTimeFeedRequestParams params) {
        try {
            log.info("Starting real-time data stream for metric: {}", params.getMetric());
            
            if (params.getMetric() == null || params.getMetric().trim().isEmpty()) {
                return Flux.error(new AdapterExceptionStreamingException(
                        "Metric parameter is required for real-time streaming", "MISSING_METRIC"));
            }
            
            return realTimeDataService.streamRealTimeData(params.getMetric())
                    .doOnNext(data -> log.debug("Streaming data: {}", data))
                    .doOnError(error -> log.error("Error in real-time data stream for metric: {}", 
                            params.getMetric(), error))
                    .onErrorMap(throwable -> {
                        if (throwable instanceof AdapterExceptionStreamingException) {
                            return throwable;
                        }
                        return new AdapterExceptionStreamingException(
                                "Failed to stream real-time data: " + throwable.getMessage(), 
                                "STREAMING_ERROR");
                    });
                    
        } catch (Exception e) {
            log.error("Error starting real-time data stream for metric: {}", params.getMetric(), e);
            return Flux.error(new AdapterExceptionStreamingException(
                    "Failed to start real-time data stream: " + e.getMessage(), "STREAM_START_ERROR"));
        }
    }

    /**
     * Alternative endpoint for WebSocket-style streaming (if needed in the future).
     * Currently using SSE approach above.
     */
    @GetMapping("/real-time-feed/ws")
    public Flux<SharedRealTimeDataDTO> streamRealTimeDataWebSocket(
            @RequestParam String metric) {
        try {
            log.info("Starting WebSocket-style real-time data stream for metric: {}", metric);
            
            if (metric == null || metric.trim().isEmpty()) {
                return Flux.error(new AdapterExceptionStreamingException(
                        "Metric parameter is required", "MISSING_METRIC"));
            }
            
            return realTimeDataService.streamRealTimeData(metric)
                    .doOnNext(data -> log.debug("WebSocket streaming data: {}", data))
                    .doOnError(error -> log.error("Error in WebSocket real-time stream for metric: {}", 
                            metric, error))
                    .onErrorMap(throwable -> new AdapterExceptionStreamingException(
                            "WebSocket streaming failed: " + throwable.getMessage(), 
                            "WEBSOCKET_STREAMING_ERROR"));
                            
        } catch (Exception e) {
            log.error("Error starting WebSocket real-time stream for metric: {}", metric, e);
            return Flux.error(new AdapterExceptionStreamingException(
                    "Failed to start WebSocket stream: " + e.getMessage(), "WEBSOCKET_START_ERROR"));
        }
    }
}