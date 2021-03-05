package edu.kpi.testcourse.exception;

import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import javax.inject.Singleton;

/**
 * Return HTTP 404 status code if an alias was not found.
 */
@Produces
@Singleton
@Requires(classes = {ShortLinkNotFoundException.class, ExceptionHandler.class})
public class ShortLinkNotFoundExceptionHandler
  implements ExceptionHandler<ShortLinkNotFoundException, HttpResponse> {
  @Override
  public HttpResponse handle(HttpRequest request, ShortLinkNotFoundException exception) {
    return HttpResponse.notFound("Link with given alias was not found");
  }
}
