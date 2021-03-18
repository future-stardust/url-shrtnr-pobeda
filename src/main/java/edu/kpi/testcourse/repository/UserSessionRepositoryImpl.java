package edu.kpi.testcourse.repository;

import edu.kpi.testcourse.bigtable.BigTableManager;
import edu.kpi.testcourse.dto.ShortLink;
import edu.kpi.testcourse.dto.UserSession;
import edu.kpi.testcourse.exception.bigtable.BigTableException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Optional;
import java.util.stream.Collectors;
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
   * Users`s sessions list.
   */
  private ArrayList<UserSession> userSessions = new ArrayList<>();

  /**
   * Returns user session object.
   *
   * @param token token
   * @return user`s session if it has been found
   */
  @Override
  public Optional<UserSession> findByToken(String token) {
    return userSessions.stream()
        .filter(s -> s.token().equals(token))
        .findFirst();
  }

  /**
   * Returns list of session of specified user.
   *
   * @param email od user
   * @return ArrayList
   */
  @Override
  public ArrayList<UserSession> getSessionsOfUser(String email) {
    return userSessions.stream()
      .filter(s -> s.userEmail().equals(email))
      .collect(Collectors.toCollection(ArrayList::new));
  }

  /**
   * Removes user session from BigTable.
   *
   * @param token of user session
   */
  @Override
  public void deleteUserSession(String token) {
    userSessions.removeIf(s -> s.token().equals(token));
  }

  /**
   * Persists user session.
   *
   * @param userSession to save
   */
  @Override
  public void saveUserSession(UserSession userSession) {
    userSessions.add(userSession);
  }
}
