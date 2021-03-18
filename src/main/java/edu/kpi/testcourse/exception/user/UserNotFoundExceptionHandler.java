package edu.kpi.testcourse.exception.user;

import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import javax.inject.Singleton;

/**
 * UserNotFound exception handler.
 */
@Produces(MediaType.TEXT_PLAIN)
@Singleton
@Requires(classes = {SignUpException.class, ExceptionHandler.class})
public class UserNotFoundExceptionHandler
    implements ExceptionHandler<UserNotFoundException, HttpResponse<?>> {

  @Override
  public HttpResponse<?> handle(HttpRequest request, UserNotFoundException exception) {
    return HttpResponse.badRequest(exception);
  }
}
