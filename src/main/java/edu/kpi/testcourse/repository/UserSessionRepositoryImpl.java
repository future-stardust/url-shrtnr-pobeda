package edu.kpi.testcourse.repository;

import edu.kpi.testcourse.bigtable.BigTableManager;
import edu.kpi.testcourse.dto.ShortLink;
import edu.kpi.testcourse.dto.UserSession;
import edu.kpi.testcourse.exception.bigtable.BigTableException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
import javax.inject.Inject;
import javax.inject.Singleton;


/**
 * Default implementation for UserSessionRepository.
 */
@Singleton
public class UserSessionRepositoryImpl implements UserSessionRepository {

  @Inject
  private BigTableManager bigTableManager;

  /**
   * Returns user session object.
   *
   * @param token token
   * @return user`s session if it has been found
   */
  @Override
  public Optional<UserSession> findByToken(String token) {
    try {
      return bigTableManager.findUserSessionByToken(token);
    } catch (IOException exception) {
      throw new BigTableException(exception);
    }
  }

  /**
   * Returns list of session of specified user.
   *
   * @param email od user
   * @return ArrayList
   */
  @Override
  public ArrayList<UserSession> getSessionsOfUser(String email) {
    try {
      return bigTableManager.listAllUserSessions(email);
    } catch (IOException exception) {
      throw new BigTableException(exception);
    }
  }

  /**
   * Removes user session from BigTable.
   *
   * @param token of user session
   */
  @Override
  public void deleteUserSession(String token) {
    try {
      Optional<UserSession> userSession = bigTableManager.findUserSessionByToken(token);
      if (!userSession.isPresent() && userSession.get().token().equals(token)) {
        bigTableManager.deleteUserSession(userSession.get().id());
      }
    } catch (IOException exception) {
      throw new BigTableException(exception);
    }
  }

  /**
   * Persists user session.
   *
   * @param userSession to save
   */
  @Override
  public void saveUserSession(UserSession userSession) {
    try {
      bigTableManager.storeUserSession(userSession);
    } catch (IOException exception) {
      throw new BigTableException(exception);
    }
  }
}
