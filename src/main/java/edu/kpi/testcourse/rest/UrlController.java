package edu.kpi.testcourse.rest;

import edu.kpi.testcourse.logic.ShortLinkServiceImpl;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Consumes;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Header;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Produces;
import java.net.URL;
import javax.annotation.Nullable;
import javax.inject.Inject;

/**
 * Controller of POST /urls/shorten, DELETE /urls/{alias}, GET /urls.
 */
@Controller("/urls")
public class UrlController {

  @Inject
  ShortLinkServiceImpl shortLinkService;

  record UserUrl(String url, String alias) {}

  /**
   * Authorized user cen shorten an URL on this route.
   *
   * @param token - bearer auth JWT token to check if user is authorized.
   * @param urlToShorten - URL with unnecessary alias that must be shortened.
   * @return - short URL.
   */
  @Consumes()
  @Produces(MediaType.APPLICATION_JSON)
  @Post("/shorten")
  public UserUrl shortenUrl(@Header("token") String token, @Body UserUrl urlToShorten) {
    return new UserUrl(urlToShorten.url, "");
  }
}
