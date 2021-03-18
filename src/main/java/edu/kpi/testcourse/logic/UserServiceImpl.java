package edu.kpi.testcourse.logic;

import edu.kpi.testcourse.dto.User;
import edu.kpi.testcourse.exception.auth.InvalidTokenException;
import edu.kpi.testcourse.exception.bigtable.BigTableException;
import edu.kpi.testcourse.exception.user.InvalidSignUpRequestException;
import edu.kpi.testcourse.exception.user.UserAlreadyExistsException;
import edu.kpi.testcourse.repository.UserRepository;
import edu.kpi.testcourse.repository.UserSessionRepository;
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
  public void signOut(String accesToken) {
    try {
      userSessionRepository.deleteUserSession(accesToken);
    } catch (BigTableException exception) {
      throw new InvalidTokenException();
    }
  }
}
