package edu.kpi.testcourse.rest;

import edu.kpi.testcourse.Main;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

/**
 * REST API controller that provides logic for Micronaut framework.
 */
@Controller
public class ApiController {

  record ExampleClass(String first, String second) {}

  @Get(value = "/hello", produces = MediaType.APPLICATION_JSON)
  public String hello() {
    return Main.getGson().toJson(new ExampleClass("Hello", "world!"));
  }

}
