package ai.shreds.domain.entities;

import ai.shreds.domain.exceptions.DomainExceptionInvalidVoteCountException;
import ai.shreds.domain.exceptions.DomainExceptionDataValidationException;
import ai.shreds.shared.dtos.SharedTotalVotesItemDTO;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public class DomainEntityTotalVotesPerCandidate {
    private final UUID candidateId;
    private final String candidateName;
    private int totalVotes;
    private final UUID pollingStationId;
    private final OffsetDateTime timestamp;

    public DomainEntityTotalVotesPerCandidate(UUID candidateId,
                                              String candidateName,
                                              int totalVotes,
                                              UUID pollingStationId,
                                              OffsetDateTime timestamp) throws DomainExceptionInvalidVoteCountException, DomainExceptionDataValidationException {
        this.candidateId = candidateId;
        this.candidateName = candidateName;
        this.totalVotes = totalVotes;
        this.pollingStationId = pollingStationId;
        this.timestamp = timestamp;
        validate();
    }

    public void validate() throws DomainExceptionInvalidVoteCountException, DomainExceptionDataValidationException {
        if (candidateId == null) {
            throw new DomainExceptionDataValidationException("Candidate ID cannot be null", List.of("candidateId"));
        }
        if (candidateName == null || candidateName.isBlank()) {
            throw new DomainExceptionDataValidationException("Candidate name cannot be blank", List.of("candidateName"));
        }
        if (totalVotes < 0) {
            throw new DomainExceptionInvalidVoteCountException("Total votes cannot be negative", totalVotes);
        }
        if (pollingStationId == null) {
            throw new DomainExceptionDataValidationException("Polling station ID cannot be null", List.of("pollingStationId"));
        }
        if (timestamp == null) {
            throw new DomainExceptionDataValidationException("Timestamp cannot be null", List.of("timestamp"));
        }
    }

    public void incrementVotes(int votes) throws DomainExceptionInvalidVoteCountException {
        if (votes < 0) {
            throw new DomainExceptionInvalidVoteCountException("Increment votes cannot be negative", votes);
        }
        this.totalVotes += votes;
    }

    public SharedTotalVotesItemDTO toDTO() {
        return new SharedTotalVotesItemDTO(
                this.candidateId,
                this.candidateName,
                this.totalVotes,
                this.pollingStationId,
                this.timestamp.toString()
        );
    }

    public static DomainEntityTotalVotesPerCandidate fromDTO(SharedTotalVotesItemDTO dto) throws DomainExceptionInvalidVoteCountException, DomainExceptionDataValidationException {
        return new DomainEntityTotalVotesPerCandidate(
                dto.getCandidateId(),
                dto.getCandidateName(),
                dto.getTotalVotes(),
                dto.getPollingStationId(),
                OffsetDateTime.parse(dto.getTimestamp())
        );
    }

    public UUID getCandidateId() {
        return candidateId;
    }

    public String getCandidateName() {
        return candidateName;
    }

    public int getTotalVotes() {
        return totalVotes;
    }

    public UUID getPollingStationId() {
        return pollingStationId;
    }

    public OffsetDateTime getTimestamp() {
        return timestamp;
    }
}