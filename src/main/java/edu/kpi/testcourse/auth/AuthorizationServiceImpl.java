package edu.kpi.testcourse.auth;

import edu.kpi.testcourse.dto.UserSession;
import edu.kpi.testcourse.exception.auth.InvalidTokenException;
import edu.kpi.testcourse.exception.auth.UnauthorizedException;
import edu.kpi.testcourse.exception.user.UserNotFoundException;
import edu.kpi.testcourse.repository.UserSessionRepository;
import java.util.HashMap;
import java.util.Optional;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Implementation of user authorization service.
 */
@Singleton
public class AuthorizationServiceImpl implements AuthorizationService {

  @Inject
  private UserSessionRepository userSessionRepository;

  @Override
  public void signOut(String accessToken) {
    try {
      Optional<UserSession> userSession = userSessionRepository.findByToken(accessToken);
      if (userSession.isEmpty()) {
        throw new UserNotFoundException();
      }
      userSessionRepository.deleteUserSession(accessToken);
    } catch (UserNotFoundException exception) {
      throw new InvalidTokenException();
    }
  }
}
