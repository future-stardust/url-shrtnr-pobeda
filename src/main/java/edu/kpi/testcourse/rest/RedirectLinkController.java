package edu.kpi.testcourse.rest;

import edu.kpi.testcourse.exception.url.ShortLinkNotFoundException;
import edu.kpi.testcourse.logic.ShortLinkService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;
import javax.inject.Inject;
import javax.validation.constraints.NotNull;

/**
 * Route /r controller.
 * GET /r - nothing
 * GET /r/:link - redirect by short link to destination
 */
@Secured(SecurityRule.IS_ANONYMOUS)
@Controller("/r")
public class RedirectLinkController {

  @Inject
  ShortLinkService shortLinkService;

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
