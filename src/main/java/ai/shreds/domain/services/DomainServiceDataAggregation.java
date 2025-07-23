package ai.shreds.domain.services;

import ai.shreds.domain.entities.DomainEntityTotalVotesPerCandidate;
import ai.shreds.domain.exceptions.DomainExceptionDataValidationException;
import ai.shreds.domain.exceptions.DomainExceptionInvalidVoteCountException;
import ai.shreds.domain.ports.DomainOutputPortTotalVotesPerCandidateRepository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DomainServiceDataAggregation {
    private final DomainOutputPortTotalVotesPerCandidateRepository totalVotesRepository;

    public DomainServiceDataAggregation(DomainOutputPortTotalVotesPerCandidateRepository totalVotesRepository) {
        this.totalVotesRepository = totalVotesRepository;
    }

    public DomainEntityTotalVotesPerCandidate aggregateTotalVotes(UUID candidateId, String candidateName, 
                                                                 Integer votes, UUID pollingStationId) 
            throws DomainExceptionInvalidVoteCountException, DomainExceptionDataValidationException {
        
        DomainEntityTotalVotesPerCandidate entity = new DomainEntityTotalVotesPerCandidate(
            candidateId, candidateName, votes, pollingStationId, OffsetDateTime.now()
        );
        
        return totalVotesRepository.save(entity);
    }

    public List<DomainEntityTotalVotesPerCandidate> getTotalVotesByCandidate(UUID candidateId) {
        return totalVotesRepository.findByCandidateId(candidateId);
    }

    public List<DomainEntityTotalVotesPerCandidate> getTotalVotesByStation(UUID pollingStationId) {
        return totalVotesRepository.findByPollingStationId(pollingStationId);
    }

    public void validateIncomingData(Map<String, Object> data) throws DomainExceptionDataValidationException {
        if (data == null || data.isEmpty()) {
            throw new DomainExceptionDataValidationException(
                "Data cannot be null or empty", List.of("data")
            );
        }
        
        if (!data.containsKey("candidateId") || data.get("candidateId") == null) {
            throw new DomainExceptionDataValidationException(
                "Candidate ID is required", List.of("candidateId")
            );
        }
        
        if (!data.containsKey("votes") || data.get("votes") == null) {
            throw new DomainExceptionDataValidationException(
                "Votes count is required", List.of("votes")
            );
        }
        
        if (!data.containsKey("pollingStationId") || data.get("pollingStationId") == null) {
            throw new DomainExceptionDataValidationException(
                "Polling station ID is required", List.of("pollingStationId")
            );
        }
    }
}