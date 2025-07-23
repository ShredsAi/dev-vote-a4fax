package ai.shreds.application.services;

import ai.shreds.application.ports.ApplicationInputPortGetStatisticalReports;
import ai.shreds.application.ports.ApplicationOutputPortMonitoring;
import ai.shreds.domain.services.DomainServiceReportGeneration;
import ai.shreds.domain.ports.DomainOutputPortStatisticalReportsRepository;
import ai.shreds.domain.entities.DomainEntityStatisticalReports;
import ai.shreds.shared.dtos.SharedStatisticalReportItemDTO;
import ai.shreds.shared.dtos.SharedMonitoringEventDTO;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.HashMap;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApplicationStatisticalReportsService implements ApplicationInputPortGetStatisticalReports {

    private final DomainServiceReportGeneration domainServiceReportGeneration;
    private final DomainOutputPortStatisticalReportsRepository domainOutputPortStatisticalReportsRepository;
    private final ApplicationOutputPortMonitoring applicationOutputPortMonitoring;

    @Override
    public List<SharedStatisticalReportItemDTO> getStatisticalReports(
            String reportType, 
            String fromTimestamp, 
            String toTimestamp
    ) {
        try {
            List<DomainEntityStatisticalReports> reports;
            
            // Apply filtering based on provided parameters
            if (fromTimestamp != null && toTimestamp != null) {
                OffsetDateTime start = OffsetDateTime.parse(fromTimestamp, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
                OffsetDateTime end = OffsetDateTime.parse(toTimestamp, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
                reports = domainOutputPortStatisticalReportsRepository.findByGeneratedAtBetween(start, end);
                
                // Further filter by report type if provided
                if (reportType != null && !reportType.isEmpty()) {
                    reports = reports.stream()
                            .filter(report -> report.getReportType().equals(reportType))
                            .collect(Collectors.toList());
                }
            } else if (reportType != null && !reportType.isEmpty()) {
                // Use domain service method for report type filtering
                reports = domainServiceReportGeneration.getReportsByType(reportType);
                
                // Apply single timestamp filtering if needed
                if (fromTimestamp != null || toTimestamp != null) {
                    reports = filterByTimeRange(reports, fromTimestamp, toTimestamp);
                }
            } else {
                reports = domainOutputPortStatisticalReportsRepository.findAll();
                
                // Apply single timestamp filtering if needed
                if (fromTimestamp != null || toTimestamp != null) {
                    reports = filterByTimeRange(reports, fromTimestamp, toTimestamp);
                }
            }

            applicationOutputPortMonitoring.recordMetric(new SharedMonitoringEventDTO(
                "statistical_reports_retrieved",
                reportType != null ? reportType : "all",
                (double) reports.size(),
                new HashMap<>() {{
                    put("reportType", reportType);
                    put("fromTimestamp", fromTimestamp);
                    put("toTimestamp", toTimestamp);
                }},
                OffsetDateTime.now().toString()
            ));

            return reports.stream()
                    .map(DomainEntityStatisticalReports::toDTO)
                    .toList();
        } catch (Exception e) {
            applicationOutputPortMonitoring.recordError(
                "Error retrieving statistical reports",
                new HashMap<>() {{
                    put("reportType", reportType);
                    put("fromTimestamp", fromTimestamp);
                    put("toTimestamp", toTimestamp);
                    put("error", e.getMessage());
                }}
            );
            throw e;
        }
    }
    
    private List<DomainEntityStatisticalReports> filterByTimeRange(
            List<DomainEntityStatisticalReports> reports, 
            String fromTimestamp, 
            String toTimestamp) {
        
        if (fromTimestamp == null && toTimestamp == null) {
            return reports;
        }
        
        OffsetDateTime start = fromTimestamp != null ? 
                OffsetDateTime.parse(fromTimestamp, DateTimeFormatter.ISO_OFFSET_DATE_TIME) : null;
        OffsetDateTime end = toTimestamp != null ? 
                OffsetDateTime.parse(toTimestamp, DateTimeFormatter.ISO_OFFSET_DATE_TIME) : null;
        
        return reports.stream()
                .filter(report -> {
                    OffsetDateTime generatedTime = report.getGeneratedAt();
                    boolean afterStart = start == null || generatedTime.isAfter(start) || generatedTime.isEqual(start);
                    boolean beforeEnd = end == null || generatedTime.isBefore(end) || generatedTime.isEqual(end);
                    return afterStart && beforeEnd;
                })
                .collect(Collectors.toList());
    }
}