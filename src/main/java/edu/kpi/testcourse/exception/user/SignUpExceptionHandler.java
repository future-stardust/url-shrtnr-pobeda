package edu.kpi.testcourse.exception.user;

import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import javax.inject.Singleton;

/**
 * Signup exception handler.
 */
@Produces(MediaType.TEXT_PLAIN)
@Singleton
@Requires(classes = {SignUpException.class, ExceptionHandler.class})
public class SignUpExceptionHandler implements ExceptionHandler<SignUpException, HttpResponse<?>> {

  @Override
  public HttpResponse<?> handle(HttpRequest request, SignUpException exception) {
    return HttpResponse.badRequest("Failed to sign up: email or password were not provided");
  }
}
