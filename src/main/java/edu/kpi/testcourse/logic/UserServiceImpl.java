package edu.kpi.testcourse.logic;

import edu.kpi.testcourse.dto.User;
import edu.kpi.testcourse.dto.UserSession;
import edu.kpi.testcourse.exception.auth.InvalidTokenException;
import edu.kpi.testcourse.exception.bigtable.BigTableException;
import edu.kpi.testcourse.exception.user.InvalidSignUpRequestException;
import edu.kpi.testcourse.exception.user.UserAlreadyExistsException;
import edu.kpi.testcourse.exception.user.UserNotFoundException;
import edu.kpi.testcourse.repository.UserRepository;
import edu.kpi.testcourse.repository.UserSessionRepository;
import java.util.Optional;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Default user service implementation.
 */
@Singleton
public class UserServiceImpl implements UserService {

  @Inject
  private UserRepository userRepository;

  @Inject
  private UserSessionRepository userSessionRepository;

  @Override
  public void signup(User user) throws InvalidSignUpRequestException, UserAlreadyExistsException {
    if (!user.isValid()) {
      throw new InvalidSignUpRequestException();
    }
    if (userRepository.findByEmail(user.email()).isPresent()) {
      throw new UserAlreadyExistsException();
    }
    userRepository.create(user);
  }

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
