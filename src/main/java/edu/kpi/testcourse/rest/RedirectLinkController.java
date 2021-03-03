package edu.kpi.testcourse.rest;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;

/**
 * Route /r controller.
 * GET /r - nothing
 * GET /r/:link - redirect by short link to destination
 */
@Controller("/r")
public class RedirectLinkController {

  /**
   * GET /r route.
   *
   * @return nothing
   */
  @Get()
  @Produces(MediaType.TEXT_PLAIN)
  public String index() {
    return "The route exists";
  }
}
