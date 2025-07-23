package ai.shreds.application.services;

import ai.shreds.application.ports.ApplicationInputPortGetTurnoutRates;
import ai.shreds.application.ports.ApplicationOutputPortMonitoring;
import ai.shreds.domain.services.DomainServiceTurnoutCalculation;
import ai.shreds.domain.ports.DomainOutputPortTurnoutRatesRepository;
import ai.shreds.domain.entities.DomainEntityTurnoutRates;
import ai.shreds.shared.dtos.SharedTurnoutRateItemDTO;
import ai.shreds.shared.dtos.SharedMonitoringEventDTO;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.HashMap;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApplicationTurnoutRatesService implements ApplicationInputPortGetTurnoutRates {

    private final DomainServiceTurnoutCalculation domainServiceTurnoutCalculation;
    private final DomainOutputPortTurnoutRatesRepository domainOutputPortTurnoutRatesRepository;
    private final ApplicationOutputPortMonitoring applicationOutputPortMonitoring;

    @Override
    public List<SharedTurnoutRateItemDTO> getTurnoutRates(
            UUID stationId, 
            String fromTimestamp, 
            String toTimestamp
    ) {
        try {
            List<DomainEntityTurnoutRates> rates;
            
            // Apply filtering based on provided parameters
            if (fromTimestamp != null && toTimestamp != null) {
                OffsetDateTime start = OffsetDateTime.parse(fromTimestamp, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
                OffsetDateTime end = OffsetDateTime.parse(toTimestamp, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
                rates = domainOutputPortTurnoutRatesRepository.findByTimestampBetween(start, end);
                
                // Further filter by station if provided
                if (stationId != null) {
                    rates = rates.stream()
                            .filter(rate -> rate.getStationId().equals(stationId))
                            .collect(Collectors.toList());
                }
            } else if (stationId != null) {
                var optRate = domainOutputPortTurnoutRatesRepository.findByStationId(stationId);
                rates = optRate.map(List::of).orElse(List.of());
            } else {
                rates = domainOutputPortTurnoutRatesRepository.findAll();
            }
            
            // Apply additional time filtering if only one timestamp bound is provided
            if ((fromTimestamp != null || toTimestamp != null) && 
                !(fromTimestamp != null && toTimestamp != null)) {
                rates = filterByTimeRange(rates, fromTimestamp, toTimestamp);
            }

            applicationOutputPortMonitoring.recordMetric(new SharedMonitoringEventDTO(
                "turnout_rates_retrieved",
                "turnout",
                (double) rates.size(),
                new HashMap<>() {{
                    put("stationId", stationId != null ? stationId.toString() : "all");
                    put("fromTimestamp", fromTimestamp);
                    put("toTimestamp", toTimestamp);
                }},
                OffsetDateTime.now().toString()
            ));

            return rates.stream()
                    .map(DomainEntityTurnoutRates::toDTO)
                    .toList();
                    
        } catch (Exception e) {
            applicationOutputPortMonitoring.recordError(
                "Error retrieving turnout rates",
                new HashMap<>() {{
                    put("stationId", stationId);
                    put("fromTimestamp", fromTimestamp);
                    put("toTimestamp", toTimestamp);
                    put("error", e.getMessage());
                }}
            );
            throw e;
        }
    }
    
    private List<DomainEntityTurnoutRates> filterByTimeRange(
            List<DomainEntityTurnoutRates> rates,
            String fromTimestamp,
            String toTimestamp) {
        
        if (fromTimestamp == null && toTimestamp == null) {
            return rates;
        }
        
        OffsetDateTime start = fromTimestamp != null ? 
                OffsetDateTime.parse(fromTimestamp, DateTimeFormatter.ISO_OFFSET_DATE_TIME) : null;
        OffsetDateTime end = toTimestamp != null ? 
                OffsetDateTime.parse(toTimestamp, DateTimeFormatter.ISO_OFFSET_DATE_TIME) : null;
        
        return rates.stream()
                .filter(rate -> {
                    OffsetDateTime timestamp = rate.getTimestamp();
                    boolean afterStart = start == null || timestamp.isAfter(start) || timestamp.isEqual(start);
                    boolean beforeEnd = end == null || timestamp.isBefore(end) || timestamp.isEqual(end);
                    return afterStart && beforeEnd;
                })
                .collect(Collectors.toList());
    }
}
