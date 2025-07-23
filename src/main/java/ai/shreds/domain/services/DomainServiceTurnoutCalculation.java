package ai.shreds.domain.services;

import ai.shreds.domain.entities.DomainEntityTurnoutRates;
import ai.shreds.domain.exceptions.DomainExceptionDataValidationException;
import ai.shreds.domain.exceptions.DomainExceptionInvalidTurnoutPercentageException;
import ai.shreds.domain.ports.DomainOutputPortTurnoutRatesRepository;
import ai.shreds.domain.value_objects.DomainValueTurnoutPercentage;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

public class DomainServiceTurnoutCalculation {
    private final DomainOutputPortTurnoutRatesRepository turnoutRatesRepository;

    public DomainServiceTurnoutCalculation(DomainOutputPortTurnoutRatesRepository turnoutRatesRepository) {
        this.turnoutRatesRepository = turnoutRatesRepository;
    }

    public DomainEntityTurnoutRates calculateTurnoutRate(UUID stationId, Integer registeredVoters, Integer votesCast) 
            throws DomainExceptionInvalidTurnoutPercentageException, DomainExceptionDataValidationException {
        
        double percentage = (registeredVoters == 0) ? 0.0 : (votesCast * 100.0 / registeredVoters);
        DomainValueTurnoutPercentage turnoutPercentage = new DomainValueTurnoutPercentage(percentage);
        
        DomainEntityTurnoutRates turnoutRate = new DomainEntityTurnoutRates(
            stationId,
            registeredVoters,
            votesCast,
            turnoutPercentage,
            OffsetDateTime.now()
        );
        
        return turnoutRatesRepository.save(turnoutRate);
    }

    public DomainEntityTurnoutRates updateTurnoutRate(UUID stationId, Integer newVotesCast) 
            throws DomainExceptionInvalidTurnoutPercentageException, DomainExceptionDataValidationException {
        
        Optional<DomainEntityTurnoutRates> existingTurnout = turnoutRatesRepository.findByStationId(stationId);
        
        if (existingTurnout.isPresent()) {
            DomainEntityTurnoutRates turnoutRate = existingTurnout.get();
            turnoutRate.updateVotesCast(newVotesCast);
            return turnoutRatesRepository.save(turnoutRate);
        } else {
            throw new DomainExceptionDataValidationException(
                "No turnout record found for station: " + stationId, 
                java.util.List.of("stationId")
            );
        }
    }

    public DomainEntityTurnoutRates getTurnoutRatesByStation(UUID stationId) {
        return turnoutRatesRepository.findByStationId(stationId).orElse(null);
    }
    
    public double calculateTurnoutPercentage(Integer registeredVoters, Integer votesCast) {
        if (registeredVoters == null || registeredVoters == 0) {
            return 0.0;
        }
        if (votesCast == null || votesCast < 0) {
            return 0.0;
        }
        return (votesCast * 100.0) / registeredVoters;
    }
}