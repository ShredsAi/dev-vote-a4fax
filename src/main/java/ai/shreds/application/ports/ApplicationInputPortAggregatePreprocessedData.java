package ai.shreds.application.ports;

import ai.shreds.shared.dtos.SharedPreprocessedDataDTO;

/**
 * Input port for aggregating a single preprocessed voting data record.
 */
public interface ApplicationInputPortAggregatePreprocessedData {

    /**
     * Aggregates a single preprocessed data record.
     *
     * @param data the preprocessed data DTO
     */
    void aggregatePreprocessedData(SharedPreprocessedDataDTO data);
}
