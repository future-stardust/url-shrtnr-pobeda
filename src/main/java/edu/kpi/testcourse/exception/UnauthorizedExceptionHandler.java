package edu.kpi.testcourse.exception;

import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import javax.inject.Singleton;

/**
 * Return HTTP 401 status code if a user is not authorized.
 */
@Produces(MediaType.TEXT_PLAIN)
@Singleton
@Requires(classes = {UnauthorizedException.class, ExceptionHandler.class})
public class UnauthorizedExceptionHandler
    implements ExceptionHandler<UnauthorizedException, HttpResponse> {
  @Override
  public HttpResponse handle(HttpRequest request, UnauthorizedException exception) {
    return HttpResponse.unauthorized();
  }
}
