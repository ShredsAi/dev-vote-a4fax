package ai.shreds.domain.ports;

import reactor.core.publisher.Flux;
import java.util.Map;
import java.util.function.Consumer;

public interface DomainOutputPortRealTimeDataRepository {
    Flux<Map<String, Object>> streamData(String metric);
    
    void publishData(String metric, Map<String, Object> data);
    
    void subscribeToMetric(String metric, Consumer<Map<String, Object>> callback);
}