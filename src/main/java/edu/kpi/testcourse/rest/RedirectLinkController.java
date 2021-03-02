package edu.kpi.testcourse.rest;

import io.micronaut.http.*;
import io.micronaut.http.annotation.*;

@Controller("/r")
public class RedirectLinkController {
  @Get()
  @Produces(MediaType.TEXT_PLAIN)
  public String index() {
    return "The route exists";
  }
}
