package ai.shreds.application.config;

import ai.shreds.application.ports.ApplicationOutputPortAuthentication;
import ai.shreds.application.ports.ApplicationOutputPortMonitoring;
import ai.shreds.application.services.*;
import ai.shreds.domain.services.*;
import ai.shreds.domain.ports.*;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ComponentScan;

/**
 * Configuration class for the application layer.
 * Defines beans and configurations for application services.
 */
@Configuration
@ComponentScan(basePackages = "ai.shreds.application")
public class ApplicationConfig {

    /**
     * Configures the total votes service bean.
     */
    @Bean
    public ApplicationTotalVotesService applicationTotalVotesService(
            DomainServiceDataAggregation domainServiceDataAggregation,
            DomainOutputPortTotalVotesPerCandidateRepository domainOutputPortTotalVotesRepository,
            ApplicationOutputPortMonitoring applicationOutputPortMonitoring) {
        return new ApplicationTotalVotesService(
                domainServiceDataAggregation,
                domainOutputPortTotalVotesRepository,
                applicationOutputPortMonitoring
        );
    }

    /**
     * Configures the voting trends service bean.
     */
    @Bean
    public ApplicationVotingTrendsService applicationVotingTrendsService(
            DomainServiceVotingTrendsAnalysis domainServiceVotingTrendsAnalysis,
            DomainOutputPortVotingTrendsOverTimeRepository domainOutputPortVotingTrendsRepository,
            ApplicationOutputPortMonitoring applicationOutputPortMonitoring) {
        return new ApplicationVotingTrendsService(
                domainServiceVotingTrendsAnalysis,
                domainOutputPortVotingTrendsRepository,
                applicationOutputPortMonitoring
        );
    }

    /**
     * Configures the turnout rates service bean.
     */
    @Bean
    public ApplicationTurnoutRatesService applicationTurnoutRatesService(
            DomainServiceTurnoutCalculation domainServiceTurnoutCalculation,
            DomainOutputPortTurnoutRatesRepository domainOutputPortTurnoutRatesRepository,
            ApplicationOutputPortMonitoring applicationOutputPortMonitoring) {
        return new ApplicationTurnoutRatesService(
                domainServiceTurnoutCalculation,
                domainOutputPortTurnoutRatesRepository,
                applicationOutputPortMonitoring
        );
    }

    /**
     * Configures the statistical reports service bean.
     */
    @Bean
    public ApplicationStatisticalReportsService applicationStatisticalReportsService(
            DomainServiceReportGeneration domainServiceReportGeneration,
            DomainOutputPortStatisticalReportsRepository domainOutputPortStatisticalReportsRepository,
            ApplicationOutputPortMonitoring applicationOutputPortMonitoring) {
        return new ApplicationStatisticalReportsService(
                domainServiceReportGeneration,
                domainOutputPortStatisticalReportsRepository,
                applicationOutputPortMonitoring
        );
    }

    /**
     * Configures the aggregated metrics service bean.
     */
    @Bean
    public ApplicationAggregatedMetricsService applicationAggregatedMetricsService(
            DomainServiceMetricCalculation domainServiceMetricCalculation,
            DomainOutputPortAggregatedMetricsRepository domainOutputPortAggregatedMetricsRepository,
            ApplicationOutputPortMonitoring applicationOutputPortMonitoring) {
        return new ApplicationAggregatedMetricsService(
                domainServiceMetricCalculation,
                domainOutputPortAggregatedMetricsRepository,
                applicationOutputPortMonitoring
        );
    }

    /**
     * Configures the data processing service bean.
     */
    @Bean
    public ApplicationDataProcessingService applicationDataProcessingService(
            ApplicationDataAggregationService applicationDataAggregationService,
            ApplicationOutputPortAuthentication applicationOutputPortAuthentication,
            ApplicationOutputPortMonitoring applicationOutputPortMonitoring) {
        return new ApplicationDataProcessingService(
                applicationDataAggregationService,
                applicationOutputPortAuthentication,
                applicationOutputPortMonitoring
        );
    }

    /**
     * Configures the real-time data service bean.
     */
    @Bean
    public ApplicationRealTimeDataService applicationRealTimeDataService(
            DomainServiceRealTimeProcessing domainServiceRealTimeProcessing,
            DomainOutputPortRealTimeDataRepository domainOutputPortRealTimeDataRepository,
            ApplicationOutputPortMonitoring applicationOutputPortMonitoring) {
        return new ApplicationRealTimeDataService(
                domainServiceRealTimeProcessing,
                domainOutputPortRealTimeDataRepository,
                applicationOutputPortMonitoring
        );
    }

    /**
     * Configures the data aggregation service bean.
     */
    @Bean
    public ApplicationDataAggregationService applicationDataAggregationService(
            DomainServiceDataAggregation domainServiceDataAggregation,
            DomainServiceVotingTrendsAnalysis domainServiceVotingTrendsAnalysis,
            DomainServiceTurnoutCalculation domainServiceTurnoutCalculation,
            DomainServiceMetricCalculation domainServiceMetricCalculation,
            ApplicationOutputPortMonitoring applicationOutputPortMonitoring) {
        return new ApplicationDataAggregationService(
                domainServiceDataAggregation,
                domainServiceVotingTrendsAnalysis,
                domainServiceTurnoutCalculation,
                domainServiceMetricCalculation,
                applicationOutputPortMonitoring
        );
    }
}