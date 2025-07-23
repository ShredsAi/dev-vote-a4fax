package ai.shreds.application.services;

import ai.shreds.application.ports.ApplicationInputPortGetTotalVotes;
import ai.shreds.application.ports.ApplicationOutputPortMonitoring;
import ai.shreds.domain.services.DomainServiceDataAggregation;
import ai.shreds.domain.ports.DomainOutputPortTotalVotesPerCandidateRepository;
import ai.shreds.shared.dtos.SharedTotalVotesItemDTO;
import ai.shreds.shared.dtos.SharedMonitoringEventDTO;
import ai.shreds.domain.entities.DomainEntityTotalVotesPerCandidate;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.HashMap;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApplicationTotalVotesService implements ApplicationInputPortGetTotalVotes {

    private final DomainServiceDataAggregation domainServiceDataAggregation;
    private final DomainOutputPortTotalVotesPerCandidateRepository domainOutputPortTotalVotesRepository;
    private final ApplicationOutputPortMonitoring applicationOutputPortMonitoring;

    @Override
    public List<SharedTotalVotesItemDTO> getTotalVotes(
            UUID candidateId,
            UUID pollingStationId,
            String fromTimestamp,
            String toTimestamp
    ) {
        try {
            List<DomainEntityTotalVotesPerCandidate> totalVotes;
            
            // Apply filtering based on provided parameters
            if (candidateId != null && pollingStationId != null) {
                // Filter by both candidate and polling station, then by time if provided
                totalVotes = domainOutputPortTotalVotesRepository.findByCandidateId(candidateId)
                        .stream()
                        .filter(vote -> vote.getPollingStationId().equals(pollingStationId))
                        .collect(Collectors.toList());
            } else if (candidateId != null) {
                totalVotes = domainOutputPortTotalVotesRepository.findByCandidateId(candidateId);
            } else if (pollingStationId != null) {
                totalVotes = domainOutputPortTotalVotesRepository.findByPollingStationId(pollingStationId);
            } else if (fromTimestamp != null && toTimestamp != null) {
                OffsetDateTime start = OffsetDateTime.parse(fromTimestamp, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
                OffsetDateTime end = OffsetDateTime.parse(toTimestamp, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
                totalVotes = domainOutputPortTotalVotesRepository.findByTimestampBetween(start, end);
            } else {
                // Fallback to get all via domain service
                totalVotes = domainServiceDataAggregation.getTotalVotesByCandidate(null);
            }
            
            // Apply time filtering if timestamps are provided and not already used for querying
            if ((fromTimestamp != null || toTimestamp != null) && candidateId != null || pollingStationId != null) {
                totalVotes = filterByTimeRange(totalVotes, fromTimestamp, toTimestamp);
            }
            
            // Record metric for monitoring
            applicationOutputPortMonitoring.recordMetric(new SharedMonitoringEventDTO(
                "total_votes_retrieved",
                "vote_count",
                (double) totalVotes.size(),
                new HashMap<>() {{
                    put("candidateId", candidateId != null ? candidateId.toString() : "all");
                    put("pollingStationId", pollingStationId != null ? pollingStationId.toString() : "all");
                }},
                OffsetDateTime.now().toString()
            ));

            return totalVotes.stream()
                    .map(DomainEntityTotalVotesPerCandidate::toDTO)
                    .toList();

        } catch (Exception e) {
            applicationOutputPortMonitoring.recordError(
                "Error retrieving total votes",
                new HashMap<>() {{
                    put("candidateId", candidateId);
                    put("pollingStationId", pollingStationId);
                    put("fromTimestamp", fromTimestamp);
                    put("toTimestamp", toTimestamp);
                    put("error", e.getMessage());
                }}
            );
            throw e;
        }
    }
    
    private List<DomainEntityTotalVotesPerCandidate> filterByTimeRange(
            List<DomainEntityTotalVotesPerCandidate> votes, 
            String fromTimestamp, 
            String toTimestamp) {
        
        if (fromTimestamp == null && toTimestamp == null) {
            return votes;
        }
        
        OffsetDateTime start = fromTimestamp != null ? 
                OffsetDateTime.parse(fromTimestamp, DateTimeFormatter.ISO_OFFSET_DATE_TIME) : null;
        OffsetDateTime end = toTimestamp != null ? 
                OffsetDateTime.parse(toTimestamp, DateTimeFormatter.ISO_OFFSET_DATE_TIME) : null;
        
        return votes.stream()
                .filter(vote -> {
                    OffsetDateTime timestamp = vote.getTimestamp();
                    boolean afterStart = start == null || timestamp.isAfter(start) || timestamp.isEqual(start);
                    boolean beforeEnd = end == null || timestamp.isBefore(end) || timestamp.isEqual(end);
                    return afterStart && beforeEnd;
                })
                .collect(Collectors.toList());
    }
}
