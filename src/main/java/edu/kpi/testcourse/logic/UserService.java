package edu.kpi.testcourse.logic;

import edu.kpi.testcourse.dto.User;
import edu.kpi.testcourse.exception.user.InvalidSignUpRequestException;
import edu.kpi.testcourse.exception.user.UserAlreadyExistsException;

/**
 * User service interface.
 */
public interface UserService {

  /**
   * Process sign up request.
   *
   * @param user Sign up request body
   * @throws InvalidSignUpRequestException Invalid request
   * @throws UserAlreadyExistsException User with this email already exists
   */
  void signup(User user) throws InvalidSignUpRequestException, UserAlreadyExistsException;
}
