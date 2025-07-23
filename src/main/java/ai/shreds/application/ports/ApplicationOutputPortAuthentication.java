package ai.shreds.application.ports;

import ai.shreds.shared.dtos.SharedAuthenticationDTO;

/**
 * Output port for authentication and authorization services.
 */
public interface ApplicationOutputPortAuthentication {

    /**
     * Authenticates a request.
     *
     * @param request authentication request data
     * @return true if authentication succeeds, false otherwise
     */
    boolean authenticate(SharedAuthenticationDTO request);

    /**
     * Authorizes a user for a specific resource and action.
     *
     * @param userId   ID of the user
     * @param resource resource to access
     * @param action   action to perform
     * @return true if authorized, false otherwise
     */
    boolean authorize(String userId, String resource, String action);
}
