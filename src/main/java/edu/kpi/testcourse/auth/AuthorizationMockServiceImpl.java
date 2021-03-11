package edu.kpi.testcourse.auth;

import edu.kpi.testcourse.exception.UnauthorizedException;
import java.util.HashMap;
import javax.inject.Singleton;

/**
 * Mock implementation of user authorization.
 * HashMap stores two users. Identify user email by key - completely random JWT token.
 */
@Singleton
public class AuthorizationMockServiceImpl implements AuthorizationMockService {
  public static final HashMap<String, String> TOKENS = new HashMap<>();

  /**
   * Put random tokens to HashMap.
   */
  public AuthorizationMockServiceImpl() {
    TOKENS.put(
        "eyJhbGciOiJIUzI1NiJ9.eyJJc3N1ZXIiOiJ1c2VyMUBtYWlsLmNvbSIsImV4cCI6MTY0NjczMDczOCwia"
          + "WF0IjoxNjE1MTk0NzM4fQ.SYb7CJl3Gx0AyeHcRGR6jWr6Gbxg0m8b7V2ZhynrYuY",
        "user1@mail.com"
    );
    TOKENS.put(
        "eyJhbGciOiJIUzI1NiJ9.eyJJc3N1ZXIiOiJ1c2VyMkBtYWlsLmNvbSIsImV4cCI6MTY0NjczMDczOCwia"
          + "WF0IjoxNjE1MTk0NzM4fQ.1VyiEw77yt998_6zNp-fxSMwpMyY93beRMMLno_uKSg",
        "user2@mail.com"
    );
  }

  /**
   * Find user in data storage by JWT token and throw an exception if a user has not been found.
   *
   * @param jwtToken key to find user.
   * @return user email.
   * @throws UnauthorizedException - a user has not been found.
   */
  @Override
  public String authorizeUser(String jwtToken)
      throws UnauthorizedException {
    String userEmail = TOKENS.get(jwtToken);
    if (userEmail == null) {
      throw new UnauthorizedException();
    } else {
      return userEmail;
    }
  }
}
