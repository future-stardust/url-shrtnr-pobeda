package edu.kpi.testcourse.repository;

import edu.kpi.testcourse.dto.UserSession;
import java.util.ArrayList;
import java.util.Optional;

/**
 * UserSession repository interface.
 */
public interface UserSessionRepository {

  /**
   * Gets user`s session from Big Table.
   *
   * @param token of session to find
   * @return UserSession by given token.
   */
  Optional<UserSession> findByToken(String token);

  /**
   * Gets all users` sessions.
   *
   * @param email of user
   * @return users` sessions
   */
  ArrayList<UserSession> getSessionsOfUser(String email);

  /**
   * Save user`s session to Big Table.
   *
   * @param userSession to save
   */
  void saveUserSession(UserSession userSession);

  /**
   * Delete session of user by token.
   *
   * @param token of yser`s session
   */
  void deleteUserSession(String token);
}
