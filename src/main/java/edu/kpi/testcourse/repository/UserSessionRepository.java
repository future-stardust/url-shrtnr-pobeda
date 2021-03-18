package edu.kpi.testcourse.repository;

import edu.kpi.testcourse.dto.UserSession;
import java.util.ArrayList;
import java.util.Optional;

/**
 * UserSession repository interface.
 */
public interface UserSessionRepository {

  Optional<UserSession> findByToken (String token);

  ArrayList<UserSession> getSessionsOfUser(String email);

  void saveUserSession(UserSession userSession);

  void deleteUserSession(String token);
}
