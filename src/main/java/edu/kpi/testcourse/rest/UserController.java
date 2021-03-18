package edu.kpi.testcourse.rest;

import com.google.gson.JsonParser;
import com.nimbusds.jose.shaded.json.JSONObject;
import edu.kpi.testcourse.dto.User;
import edu.kpi.testcourse.exception.user.SignUpException;
import edu.kpi.testcourse.logic.UserService;
import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Consumes;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientException;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.UsernamePasswordCredentials;
import io.micronaut.security.rules.SecurityRule;
import javax.inject.Inject;

/**
 * Controller of POST /users/signin, POST /users/signup, GET /users/signout.
 */
@Controller("/users")
public class UserController {

  @Inject
  private UserService userService;

  @Inject
  @Client("/")
  RxHttpClient client;

  @Consumes(MediaType.APPLICATION_JSON)
  @Secured(SecurityRule.IS_ANONYMOUS)
  @Produces(MediaType.TEXT_PLAIN)
  @Post(value = "/signup")
  public HttpResponse<?> signUp(@Body User user) throws SignUpException {
    userService.signup(user);
    return HttpResponse.ok();
  }

  /**
   * Sign in endpoint.
   *
   * <p>Converts input data of User to UsernamePasswordCredentials,
   * then sends auth request to /login entrypoint of Micronaut.
   * If user credentials are correct, this method
   * returns access token.</p>
   *
   * @param user user email and password
   * @return  200 OK - access token
   *          400 Bad Request - if credentials are wrong
   *          404 Not Found - if user not found
   */
  @Consumes(MediaType.APPLICATION_JSON)
  @Secured(SecurityRule.IS_ANONYMOUS)
  @Post(value = "/signin")
  public HttpResponse<?> signIn(@Body User user) {
    if (!user.isValid()) {
      return HttpResponse.badRequest("Credentials must be not empty");
    }
    UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(
        user.email(),
        user.password()
    );

    HttpRequest<UsernamePasswordCredentials> request = HttpRequest.POST("/login", credentials);

    String tokenBody;
    try {
      tokenBody = client.retrieve(request, String.class).blockingFirst();
    } catch (HttpClientException e) {
      if (e.getMessage().equals("User Not Found")) {
        return HttpResponse.notFound(e.getMessage());
      }
      return HttpResponse.badRequest(e.getMessage());
    }
    String token = JsonParser
        .parseString(tokenBody)
        .getAsJsonObject()
        .get("access_token").getAsString();
    JSONObject jsonResponse = new JSONObject();
    jsonResponse.put("token", token);
    return HttpResponse.ok(jsonResponse.toJSONString());
  }

  /**
   * Sign out endpoint. Provides user logout.
   *
   * @param httpHeaders HTTP headers reference
   * @return 200 OK - log out
   *         401 Unathorized - if user is not authorized
   */
  @Secured(SecurityRule.IS_AUTHENTICATED)
  @Get(value = "/signout")
  public HttpResponse<String> signOut(HttpHeaders httpHeaders) {
    String authorizationHeader = httpHeaders.getAuthorization().get();
    String accesToken = authorizationHeader.replace("Bearer ", "");
    userService.signOut(accesToken);
    return HttpResponse.ok("Signed out.");
  }
}
