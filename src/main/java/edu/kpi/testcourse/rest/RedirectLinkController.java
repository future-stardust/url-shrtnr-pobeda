package edu.kpi.testcourse.rest;

import edu.kpi.testcourse.exception.url.ShortLinkNotFoundException;
import edu.kpi.testcourse.logic.ShortLinkServiceImpl;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Optional;
import javax.inject.Inject;
import javax.validation.constraints.NotNull;

/**
 * Route /r controller.
 * GET /r - nothing
 * GET /r/:link - redirect by short link to destination
 */
@Controller("/r")
public class RedirectLinkController {

  @Inject
  ShortLinkServiceImpl shortLinkService;

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

  /**
   * GET /r/{link} - redirect by short link to destination by any user.
   *
   * @param link short link
   * @return redirect by corresponding destination link
   * @throws URISyntaxException invalid redirect link
   */
  @Get("/{link}")
  public HttpResponse<String> redirectByLink(@NotNull String link)
      throws URISyntaxException {
    Optional<String> redirectUrl = shortLinkService.getDestinationByShortLink(link);

    if (redirectUrl.isEmpty()) {
      throw new ShortLinkNotFoundException();
    } else {
      URI location = new URI(redirectUrl.get());

      return HttpResponse.redirect(location);
    }
  }
}
