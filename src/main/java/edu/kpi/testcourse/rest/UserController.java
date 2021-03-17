package edu.kpi.testcourse.rest;

import edu.kpi.testcourse.dto.User;
import edu.kpi.testcourse.exception.user.InvalidSignUpRequestException;
import edu.kpi.testcourse.exception.user.SignUpException;
import edu.kpi.testcourse.exception.user.UserAlreadyExistsException;
import edu.kpi.testcourse.logic.UserService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import javax.inject.Inject;

/**
 * Default user controller implementation.
 */
@Controller("/users")
public class UserController {

  @Inject
  private UserService userService;

  @Post(value = "/signup")
  public HttpResponse<String> signup(@Body User user) throws SignUpException {
    userService.signup(user);
    return HttpResponse.created("");
  }
}
