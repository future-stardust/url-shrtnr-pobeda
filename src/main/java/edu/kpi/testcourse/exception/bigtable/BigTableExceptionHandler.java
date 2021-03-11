package edu.kpi.testcourse.exception.bigtable;

import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import javax.inject.Singleton;

/**
 * Return 400 status code if something is wrong with BigTable.
 */
@Produces
@Singleton
@Requires(classes = {BigTableException.class, ExceptionHandler.class})
public class BigTableExceptionHandler implements ExceptionHandler<BigTableException, HttpResponse> {
  @Override
  public HttpResponse handle(HttpRequest request, BigTableException exception) {
    return HttpResponse.badRequest(exception.getMessage());
  }
}
