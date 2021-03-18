package edu.kpi.testcourse.rest;

import edu.kpi.testcourse.Main;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import java.security.Principal;

/**
 * REST API controller that provides logic for Micronaut framework.
 */
@Controller
public class ApiController {

  record ExampleClass(String first, String second) {}

  @Secured(SecurityRule.IS_AUTHENTICATED)
  @Get(value = "/hello", produces = MediaType.APPLICATION_JSON)
  public String hello(Principal principal) {
    String email = principal.getName();
    return Main.getGson().toJson(new ExampleClass("Hello", "world!"));
  }

}
