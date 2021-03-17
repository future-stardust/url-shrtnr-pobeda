package edu.kpi.testcourse.exception.user;

import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import javax.inject.Singleton;

@Produces(MediaType.TEXT_PLAIN)
@Singleton
@Requires(classes = {UserAlreadyExistsException.class, ExceptionHandler.class})
public class UserAlreadyExistsExceptionHandler implements
    ExceptionHandler<UserAlreadyExistsException, HttpResponse<?>> {
  @Override
  public HttpResponse<?> handle(HttpRequest request, UserAlreadyExistsException exception) {
    return HttpResponse.badRequest("This user already exists");
  }
}
