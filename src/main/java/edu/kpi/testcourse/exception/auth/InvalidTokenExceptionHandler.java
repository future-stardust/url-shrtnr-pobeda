package edu.kpi.testcourse.exception.auth;

import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import javax.inject.Singleton;

@Produces
@Singleton
@Requires(classes = {InvalidTokenException.class, ExceptionHandler.class})
class InvalidTokenExceptionHandler implements
    ExceptionHandler<InvalidTokenException, HttpResponse<String>> {

  @Override
  public HttpResponse<String> handle(HttpRequest request, InvalidTokenException exception) {
    return HttpResponse.unauthorized().body("Invalid token");
  }
}
