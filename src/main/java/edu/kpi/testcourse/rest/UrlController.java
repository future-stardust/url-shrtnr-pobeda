package edu.kpi.testcourse.rest;

import edu.kpi.testcourse.auth.AuthorizationMockServiceImpl;
import edu.kpi.testcourse.dto.LinksOfUser;
import edu.kpi.testcourse.dto.ShortLink;
import edu.kpi.testcourse.helper.JsonToolJacksonImpl;
import edu.kpi.testcourse.logic.ShortLinkServiceImpl;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Consumes;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Header;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Produces;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import java.util.Collections;
import javax.inject.Inject;
import javax.validation.constraints.NotNull;

/**
 * Controller of POST /urls/shorten, DELETE /urls/{alias}, GET /urls.
 */
@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller("/urls")
public class UrlController {

  @Inject
  ShortLinkServiceImpl shortLinkService;

  @Inject
  AuthorizationMockServiceImpl authorizationMockService;

  @Inject
  JsonToolJacksonImpl jsonTool;

  /**
   * Authorized user can shorten a URL on this route.
   *
   * @param token - bearer auth JWT token to check if user is authorized.
   * @param urlToShorten - URL with unnecessary alias that must be shortened.
   * @return - short URL.
   */
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.TEXT_PLAIN)
  @Post("/shorten")
  public String shortenUrl(@Header("token") String token, @Body ShortLink urlToShorten) {
    String email = this.authorizationMockService.authorizeUser(token);

    ShortLink shortLink;

    if (urlToShorten.alias() != null) {
      shortLink = this.shortLinkService.saveLink(
        email,
        urlToShorten.url(),
        urlToShorten.alias()
      );
    } else {
      shortLink = this.shortLinkService.saveLink(
        email,
        urlToShorten.url()
      );
    }
    String fullShortLink = ShortLinkServiceImpl.createFullLink(shortLink.alias());

    return jsonTool.toJson(Collections.singletonMap("shortened_url", fullShortLink));
  }

  /**
   * An authorized user can delete an alias created by him.
   * DELETE /urls/{alias}
   *
   * @param token token of user
   * @param alias alias to delete
   * @return the URL has been deleted or not
   */
  @Delete("/{alias}")
  public HttpResponse<String> deleteUrl(@Header String token, @NotNull String alias) {
    String email = this.authorizationMockService.authorizeUser(token);
    boolean wasRemoved = this.shortLinkService.deleteLinkIfBelongsToUser(email, alias);

    if (!wasRemoved) {
      return HttpResponse.notFound();
    } else {
      return HttpResponse.noContent();
    }
  }

  /**
   * Get urls that an authorized user had created.
   *
   * @param token jwt token the user is identified by
   * @return list of urls
   */
  @Get()
  public MutableHttpResponse<String> getUserUrls(@Header String token) {
    String email = this.authorizationMockService.authorizeUser(token);
    LinksOfUser linksOfUser = new LinksOfUser(this.shortLinkService.getLinksByUserEmail(email));

    return HttpResponse.ok(jsonTool.toJson(linksOfUser));
  }
}
