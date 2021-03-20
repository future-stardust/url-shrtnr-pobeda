package edu.kpi.testcourse.auth;

import edu.kpi.testcourse.exception.auth.UnauthorizedException;

/**
 * Service to provide user authorization by completely random JWT token.
 */
public interface AuthorizationService {

  /**
   * Process sign out request.
   *
   * @param accesToken active user token
   */
  void signOut(String accesToken);
}
