package edu.kpi.testcourse.auth;

import edu.kpi.testcourse.exception.auth.UnauthorizedException;

/**
 * Mock service to provide user authorization by completely random JWT token.
 */
public interface AuthorizationMockService {
  String authorizeUser(String jwtToken) throws UnauthorizedException;
}
