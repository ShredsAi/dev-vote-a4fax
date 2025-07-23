package ai.shreds.infrastructure.external_services;

import ai.shreds.application.ports.ApplicationOutputPortAuthentication;
import ai.shreds.infrastructure.exceptions.InfrastructureExceptionExternalServiceException;
import ai.shreds.shared.dtos.SharedAuthenticationDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class InfrastructureClientAuthentication implements ApplicationOutputPortAuthentication {

    private static final Logger logger = LoggerFactory.getLogger(InfrastructureClientAuthentication.class);

    private final String authServiceUrl;
    private final RestTemplate restTemplate;

    public InfrastructureClientAuthentication(
            @Value("${auth.service.url:http://localhost:8081/auth}") String authServiceUrl,
            RestTemplate restTemplate) {
        this.authServiceUrl = authServiceUrl;
        this.restTemplate = restTemplate;
    }

    @Override
    public boolean authenticate(SharedAuthenticationDTO request) {
        try {
            logger.debug("Attempting to authenticate user: {}", request.getUserId());
            
            String url = authServiceUrl + "/authenticate";
            
            // Create request headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            if (request.getToken() != null && !request.getToken().isEmpty()) {
                headers.setBearerAuth(request.getToken());
            }
            
            // Create request body
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("userId", request.getUserId());
            requestBody.put("token", request.getToken());
            requestBody.put("timestamp", request.getTimestamp());
            
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
            
            // Make the request
            ResponseEntity<Map> response = restTemplate.exchange(
                url, 
                HttpMethod.POST, 
                entity, 
                Map.class
            );
            
            // Check response
            if (response.getStatusCode() == HttpStatus.OK) {
                Map<String, Object> responseBody = response.getBody();
                if (responseBody != null && responseBody.containsKey("authenticated")) {
                    boolean authenticated = (Boolean) responseBody.get("authenticated");
                    logger.debug("Authentication result for user {}: {}", request.getUserId(), authenticated);
                    return authenticated;
                }
            }
            
            logger.warn("Authentication failed for user: {} - Invalid response", request.getUserId());
            return false;
            
        } catch (Exception e) {
            logger.error("Authentication error for user: {} - {}", request.getUserId(), e.getMessage());
            throw new InfrastructureExceptionExternalServiceException(
                "Authentication service error: " + e.getMessage(),
                "AUTH_SERVICE",
                503
            );
        }
    }

    @Override
    public boolean authorize(String userId, String resource, String action) {
        try {
            logger.debug("Attempting to authorize user: {} for resource: {} and action: {}", userId, resource, action);
            
            String url = authServiceUrl + "/authorize";
            
            // Create request headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            // Create request body
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("userId", userId);
            requestBody.put("resource", resource);
            requestBody.put("action", action);
            
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
            
            // Make the request
            ResponseEntity<Map> response = restTemplate.exchange(
                url, 
                HttpMethod.POST, 
                entity, 
                Map.class
            );
            
            // Check response
            if (response.getStatusCode() == HttpStatus.OK) {
                Map<String, Object> responseBody = response.getBody();
                if (responseBody != null && responseBody.containsKey("authorized")) {
                    boolean authorized = (Boolean) responseBody.get("authorized");
                    logger.debug("Authorization result for user {}: {}", userId, authorized);
                    return authorized;
                }
            }
            
            logger.warn("Authorization failed for user: {} - Invalid response", userId);
            return false;
            
        } catch (Exception e) {
            logger.error("Authorization error for user: {} - {}", userId, e.getMessage());
            throw new InfrastructureExceptionExternalServiceException(
                "Authorization service error: " + e.getMessage(),
                "AUTH_SERVICE",
                503
            );
        }
    }

    /**
     * Health check method to verify if the authentication service is available
     */
    public boolean isServiceAvailable() {
        try {
            String url = authServiceUrl + "/health";
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            return response.getStatusCode() == HttpStatus.OK;
        } catch (Exception e) {
            logger.warn("Authentication service health check failed: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Validate token method to check if a token is still valid
     */
    public boolean validateToken(String token) {
        try {
            String url = authServiceUrl + "/validate-token";
            
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            
            HttpEntity<Void> entity = new HttpEntity<>(headers);
            
            ResponseEntity<Map> response = restTemplate.exchange(
                url, 
                HttpMethod.POST, 
                entity, 
                Map.class
            );
            
            if (response.getStatusCode() == HttpStatus.OK) {
                Map<String, Object> responseBody = response.getBody();
                return responseBody != null && 
                       responseBody.containsKey("valid") && 
                       (Boolean) responseBody.get("valid");
            }
            
            return false;
            
        } catch (Exception e) {
            logger.error("Token validation error: {}", e.getMessage());
            return false;
        }
    }
}