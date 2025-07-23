package ai.shreds.application.ports;

import java.util.List;
import ai.shreds.shared.dtos.SharedPreprocessedDataDTO;

/**
 * Input port for processing a batch of preprocessed data.
 */
public interface ApplicationInputPortProcessData {

    /**
     * Processes a list of preprocessed data records.
     *
     * @param data list of preprocessed data DTOs to process
     */
    void processData(List<SharedPreprocessedDataDTO> data);
}
