package ai.shreds.domain.entities;

import ai.shreds.domain.exceptions.DomainExceptionDataValidationException;
import ai.shreds.domain.exceptions.DomainExceptionInvalidTurnoutPercentageException;
import ai.shreds.domain.value_objects.DomainValueTurnoutPercentage;
import ai.shreds.shared.dtos.SharedTurnoutRateItemDTO;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public class DomainEntityTurnoutRates {

    private final UUID stationId;
    private final int registeredVoters;
    private int votesCast;
    private DomainValueTurnoutPercentage turnoutPercentage;
    private final OffsetDateTime timestamp;

    public DomainEntityTurnoutRates(UUID stationId,
                                    int registeredVoters,
                                    int votesCast,
                                    DomainValueTurnoutPercentage turnoutPercentage,
                                    OffsetDateTime timestamp)
            throws DomainExceptionInvalidTurnoutPercentageException, DomainExceptionDataValidationException {
        this.stationId = stationId;
        this.registeredVoters = registeredVoters;
        this.votesCast = votesCast;
        this.turnoutPercentage = turnoutPercentage;
        this.timestamp = timestamp;
        validate();
    }

    public void validate() throws DomainExceptionDataValidationException {
        if (stationId == null) {
            throw new DomainExceptionDataValidationException(
                "Station ID cannot be null", List.of("stationId")
            );
        }
        if (registeredVoters < 0) {
            throw new DomainExceptionDataValidationException(
                "Registered voters cannot be negative", List.of("registeredVoters")
            );
        }
        if (votesCast < 0 || votesCast > registeredVoters) {
            throw new DomainExceptionDataValidationException(
                "Votes cast must be between 0 and registered voters", List.of("votesCast")
            );
        }
        if (timestamp == null) {
            throw new DomainExceptionDataValidationException(
                "Timestamp cannot be null", List.of("timestamp")
            );
        }
    }

    public void updateVotesCast(int newVotesCast)
            throws DomainExceptionDataValidationException, DomainExceptionInvalidTurnoutPercentageException {
        if (newVotesCast < 0 || newVotesCast > registeredVoters) {
            throw new DomainExceptionDataValidationException(
                "Votes cast must be between 0 and registered voters", List.of("votesCast")
            );
        }
        this.votesCast = newVotesCast;
        calculateTurnoutPercentage();
    }

    public void calculateTurnoutPercentage() throws DomainExceptionInvalidTurnoutPercentageException {
        double percent = (registeredVoters == 0) ? 0.0 : (votesCast * 100.0 / registeredVoters);
        this.turnoutPercentage = new DomainValueTurnoutPercentage(percent);
    }

    public SharedTurnoutRateItemDTO toDTO() {
        return new SharedTurnoutRateItemDTO(
                this.stationId,
                this.registeredVoters,
                this.votesCast,
                this.turnoutPercentage.getValue(),
                this.timestamp.toString()
        );
    }

    public static DomainEntityTurnoutRates fromDTO(SharedTurnoutRateItemDTO dto)
            throws DomainExceptionInvalidTurnoutPercentageException, DomainExceptionDataValidationException {
        DomainValueTurnoutPercentage percentage = new DomainValueTurnoutPercentage(dto.getTurnoutPercentage());
        return new DomainEntityTurnoutRates(
            dto.getStationId(),
            dto.getRegisteredVoters(),
            dto.getVotesCast(),
            percentage,
            OffsetDateTime.parse(dto.getTimestamp())
        );
    }

    public UUID getStationId() {
        return stationId;
    }

    public int getRegisteredVoters() {
        return registeredVoters;
    }

    public int getVotesCast() {
        return votesCast;
    }

    public DomainValueTurnoutPercentage getTurnoutPercentage() {
        return turnoutPercentage;
    }

    public OffsetDateTime getTimestamp() {
        return timestamp;
    }
}