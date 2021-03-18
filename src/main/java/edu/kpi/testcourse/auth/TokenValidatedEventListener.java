package edu.kpi.testcourse.auth;

import edu.kpi.testcourse.exception.auth.InvalidTokenException;
import edu.kpi.testcourse.exception.bigtable.BigTableException;
import edu.kpi.testcourse.repository.UserSessionRepository;
import io.micronaut.context.event.ApplicationEventListener;
import io.micronaut.security.event.TokenValidatedEvent;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Listener of TokenValidatedEvent, provided for checking if a token exists in active sessions.
 */
@Singleton
public class TokenValidatedEventListener implements ApplicationEventListener<TokenValidatedEvent> {

  @Inject
  UserSessionRepository userSessionRepository;

  /**
   * Triggered after validating token for correct structure and expiration. This method is used to
   * check for the presence of a token in UserSessionRepository
   *
   * @param event event object
   * @throws InvalidTokenException if a token is not in sessions
   */
  @Override
  public void onApplicationEvent(TokenValidatedEvent event) throws InvalidTokenException {
    String accessToken = event.getSource().toString();

    try {
      userSessionRepository.findByToken(accessToken);
    } catch (BigTableException exc) {
      throw new InvalidTokenException();
    }
  }

  @Override
  public boolean supports(TokenValidatedEvent event) {
    return ApplicationEventListener.super.supports(event);
  }
}
