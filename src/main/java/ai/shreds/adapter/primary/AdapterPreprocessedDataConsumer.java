package ai.shreds.adapter.primary;

import ai.shreds.application.ports.ApplicationInputPortAggregatePreprocessedData;
import ai.shreds.shared.dtos.SharedPreprocessedDataDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

/**
 * Kafka consumer for processing preprocessed voting data messages.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class AdapterPreprocessedDataConsumer {

    private final ApplicationInputPortAggregatePreprocessedData dataAggregationService;

    /**
     * Consumes preprocessed data messages from Kafka queue.
     */
    @KafkaListener(
            topics = "preprocessed-data-queue", 
            groupId = "aggregation-consumer-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consumePreprocessedData(
            @Payload SharedPreprocessedDataDTO message,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
            @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
            @Header(KafkaHeaders.OFFSET) long offset,
            Acknowledgment acknowledgment) {
        
        try {
            log.debug("Received preprocessed data message from topic: {}, partition: {}, offset: {}", 
                    topic, partition, offset);
            log.debug("Message content: {}", message);
            
            // Validate the incoming message
            if (message == null) {
                log.warn("Received null message, skipping processing");
                acknowledgment.acknowledge();
                return;
            }
            
            if (message.getCandidateId() == null) {
                log.warn("Received message with null candidate ID, skipping: {}", message);
                acknowledgment.acknowledge();
                return;
            }
            
            if (message.getPollingStationId() == null) {
                log.warn("Received message with null polling station ID, skipping: {}", message);
                acknowledgment.acknowledge();
                return;
            }
            
            if (message.getVotes() == null || message.getVotes() < 0) {
                log.warn("Received message with invalid vote count, skipping: {}", message);
                acknowledgment.acknowledge();
                return;
            }
            
            // Process the message
            log.info("Processing preprocessed data for candidate: {}, station: {}, votes: {}", 
                    message.getCandidateId(), message.getPollingStationId(), message.getVotes());
                    
            dataAggregationService.aggregatePreprocessedData(message);
            
            // Acknowledge successful processing
            acknowledgment.acknowledge();
            
            log.debug("Successfully processed and acknowledged message from offset: {}", offset);
            
        } catch (Exception e) {
            log.error("Error processing preprocessed data message from topic: {}, partition: {}, offset: {}", 
                    topic, partition, offset, e);
            log.error("Problematic message: {}", message);
            
            // In a production environment, you might want to:
            // 1. Send to a dead letter queue
            // 2. Retry with exponential backoff
            // 3. Alert monitoring systems
            // For now, we'll acknowledge to prevent infinite retry
            acknowledgment.acknowledge();
        }
    }
    
    /**
     * Alternative consumer method for batch processing if needed.
     * This method is commented out as we're using single message processing above.
     */
    /*
    @KafkaListener(
            topics = "preprocessed-data-queue", 
            groupId = "aggregation-batch-consumer-group",
            containerFactory = "batchKafkaListenerContainerFactory"
    )
    public void consumePreprocessedDataBatch(
            List<SharedPreprocessedDataDTO> messages,
            Acknowledgment acknowledgment) {
        
        try {
            log.info("Received batch of {} preprocessed data messages", messages.size());
            
            for (SharedPreprocessedDataDTO message : messages) {
                if (message != null && message.getCandidateId() != null && 
                    message.getPollingStationId() != null && message.getVotes() != null) {
                    dataAggregationService.aggregatePreprocessedData(message);
                }
            }
            
            acknowledgment.acknowledge();
            log.info("Successfully processed batch of {} messages", messages.size());
            
        } catch (Exception e) {
            log.error("Error processing batch of preprocessed data messages", e);
            acknowledgment.acknowledge();
        }
    }
    */
}