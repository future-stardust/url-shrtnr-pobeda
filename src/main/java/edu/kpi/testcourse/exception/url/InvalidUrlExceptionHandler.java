package edu.kpi.testcourse.exception.url;

import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import javax.inject.Singleton;

/**
 * Return HTTP 400 status code if user provides a wrong URL to shorten.
 */
@Produces
@Singleton
@Requires(classes = {InvalidUrlException.class, ExceptionHandler.class})
public class InvalidUrlExceptionHandler
    implements ExceptionHandler<InvalidUrlException, HttpResponse> {
  @Override
  public HttpResponse handle(HttpRequest request, InvalidUrlException exception) {
    return HttpResponse.badRequest(exception.getMessage());
  }
}
