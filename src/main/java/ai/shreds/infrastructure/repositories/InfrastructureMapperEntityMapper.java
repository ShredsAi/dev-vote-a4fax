package ai.shreds.infrastructure.repositories;

import ai.shreds.domain.entities.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface InfrastructureMapperEntityMapper {

    InfrastructureMapperEntityMapper INSTANCE = Mappers.getMapper(InfrastructureMapperEntityMapper.class);

    // TotalVotesPerCandidate mappings
    @Mapping(target = "candidateId", source = "candidateId")
    @Mapping(target = "candidateName", source = "candidateName") 
    @Mapping(target = "totalVotes", source = "totalVotes")
    @Mapping(target = "pollingStationId", source = "pollingStationId")
    @Mapping(target = "timestamp", source = "timestamp")
    InfrastructureJpaEntityTotalVotesPerCandidate toJpaEntity(DomainEntityTotalVotesPerCandidate domain);

    @Mapping(target = "candidateId", source = "candidateId")
    @Mapping(target = "candidateName", source = "candidateName")
    @Mapping(target = "totalVotes", source = "totalVotes")
    @Mapping(target = "pollingStationId", source = "pollingStationId")
    @Mapping(target = "timestamp", source = "timestamp")
    DomainEntityTotalVotesPerCandidate toDomainEntity(InfrastructureJpaEntityTotalVotesPerCandidate jpa);

    // VotingTrendsOverTime mappings
    @Mapping(target = "trendId", source = "trendId")
    @Mapping(target = "metric", source = "metric")
    @Mapping(target = "timeInterval", source = "timeInterval")
    @Mapping(target = "value", source = "value")
    @Mapping(target = "timestamp", source = "timestamp")
    InfrastructureJpaEntityVotingTrendsOverTime toJpaEntity(DomainEntityVotingTrendsOverTime domain);

    @Mapping(target = "trendId", source = "trendId")
    @Mapping(target = "metric", source = "metric")
    @Mapping(target = "timeInterval", source = "timeInterval")
    @Mapping(target = "value", source = "value")
    @Mapping(target = "timestamp", source = "timestamp")
    DomainEntityVotingTrendsOverTime toDomainEntity(InfrastructureJpaEntityVotingTrendsOverTime jpa);

    // TurnoutRates mappings
    @Mapping(target = "stationId", source = "stationId")
    @Mapping(target = "registeredVoters", source = "registeredVoters")
    @Mapping(target = "votesCast", source = "votesCast")
    @Mapping(target = "turnoutPercentage", source = "turnoutPercentage.value")
    @Mapping(target = "timestamp", source = "timestamp")
    InfrastructureJpaEntityTurnoutRates toJpaEntity(DomainEntityTurnoutRates domain);

    @Mapping(target = "stationId", source = "stationId")
    @Mapping(target = "registeredVoters", source = "registeredVoters")
    @Mapping(target = "votesCast", source = "votesCast")
    @Mapping(target = "turnoutPercentage", expression = "java(new ai.shreds.domain.value_objects.DomainValueTurnoutPercentage(jpa.getTurnoutPercentage()))")
    @Mapping(target = "timestamp", source = "timestamp")
    DomainEntityTurnoutRates toDomainEntity(InfrastructureJpaEntityTurnoutRates jpa);

    // StatisticalReports mappings
    @Mapping(target = "reportId", source = "reportId")
    @Mapping(target = "reportType", source = "reportType")
    @Mapping(target = "generatedAt", source = "generatedAt")
    @Mapping(target = "dataSummary", source = "dataSummary")
    @Mapping(target = "timestamp", source = "timestamp")
    InfrastructureJpaEntityStatisticalReports toJpaEntity(DomainEntityStatisticalReports domain);

    @Mapping(target = "reportId", source = "reportId")
    @Mapping(target = "reportType", source = "reportType")
    @Mapping(target = "generatedAt", source = "generatedAt")
    @Mapping(target = "dataSummary", source = "dataSummary")
    @Mapping(target = "timestamp", source = "timestamp")
    DomainEntityStatisticalReports toDomainEntity(InfrastructureJpaEntityStatisticalReports jpa);

    // AggregatedMetrics mappings
    @Mapping(target = "metricId", source = "metricId")
    @Mapping(target = "metricName", source = "metricName")
    @Mapping(target = "metricValue", source = "metricValue")
    @Mapping(target = "timestamp", source = "timestamp")
    InfrastructureJpaEntityAggregatedMetrics toJpaEntity(DomainEntityAggregatedMetrics domain);

    @Mapping(target = "metricId", source = "metricId")
    @Mapping(target = "metricName", source = "metricName")
    @Mapping(target = "metricValue", source = "metricValue")
    @Mapping(target = "timestamp", source = "timestamp")
    DomainEntityAggregatedMetrics toDomainEntity(InfrastructureJpaEntityAggregatedMetrics jpa);

    // List mappings - standardized naming
    List<DomainEntityTotalVotesPerCandidate> toDomainEntityList(List<InfrastructureJpaEntityTotalVotesPerCandidate> jpaList);
    List<DomainEntityVotingTrendsOverTime> toDomainEntityList(List<InfrastructureJpaEntityVotingTrendsOverTime> jpaList);
    List<DomainEntityTurnoutRates> toDomainEntityList(List<InfrastructureJpaEntityTurnoutRates> jpaList);
    List<DomainEntityStatisticalReports> toDomainEntityList(List<InfrastructureJpaEntityStatisticalReports> jpaList);
    List<DomainEntityAggregatedMetrics> toDomainEntityList(List<InfrastructureJpaEntityAggregatedMetrics> jpaList);
}