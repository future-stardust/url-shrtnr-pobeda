package edu.kpi.testcourse.rest;

import edu.kpi.testcourse.dto.LinksOfUser;
import edu.kpi.testcourse.dto.ShortLink;
import edu.kpi.testcourse.dto.User;
import edu.kpi.testcourse.helper.JsonTool;
import edu.kpi.testcourse.helper.JsonToolJacksonImpl;
import io.micronaut.context.ApplicationContext;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import java.util.TreeMap;
import javax.inject.Inject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@MicronautTest
public class ShortenLinkTest {
  private static String userToken;
  private static final String password = "pwd123";
  private static final User user = new User("user1@mail.com", password);

  private static EmbeddedServer server;
  private static HttpClient client;

  @Inject
  JsonTool jsonTool;

  /**
   * Get shortened URL from POST /urls/shorten response.
   *
   * @param body { "shortened_url": "localhost:8080/r/4rT7uu6Y }
   * @return the value of "shortened_url" field
   */
  private static String getShortenedUrlFromResponseBody(String body) {
    // as jsonTool can be injected but cannot be retrieved in static context,
    // create it as local field.
    JsonToolJacksonImpl jsonTool = new JsonToolJacksonImpl();

    TreeMap<String, String> parsed = jsonTool.fromJson(body, TreeMap.class);
    return parsed.get("shortened_url");
  }

  @BeforeAll
  public static void setupServer() {
    // as jsonTool can be injected but cannot be retrieved in static context,
    // create it as local field.
    JsonToolJacksonImpl jsonTool = new JsonToolJacksonImpl();

    server = ApplicationContext.run(EmbeddedServer.class);
    client = server
      .getApplicationContext()
      .createBean(HttpClient.class, server.getURL());

    // register our user
    client.toBlocking().exchange(
      HttpRequest.POST(
        "/users/signup",
        jsonTool.toJson(user)
      )
    );

    // sign in
    userToken = "Bearer " + client.toBlocking().exchange(
      HttpRequest.POST(
        "/users/signin",
        jsonTool.toJson(user)
      )
    ).header("token");
  }

  @AfterAll
  public static void stopServer() {
    if (server != null) {
      server.stop();
    }
    if (client != null) {
      client.stop();
    }
  }

  @Test
  public void shouldThrowErrorWithoutToken() {
    String requestBody = jsonTool.toJson(
      new ShortLink(
        null,
        null,
        "https://devblogs.microsoft.com/typescript/announcing-the-new-typescript-handbook/"
      )
    );

    HttpClientResponseException e = assertThrows(
      HttpClientResponseException.class,
      () -> client.toBlocking().retrieve(
        HttpRequest.POST("/urls/shorten", requestBody)
      )
    );

    assertEquals(401, e.getStatus().getCode());
  }

  @Test
  public void shouldThrowErrorWithBadToken() {
    String requestBody = jsonTool.toJson(
      new ShortLink(
        null,
        null,
        "https://devblogs.microsoft.com/typescript/announcing-the-new-typescript-handbook/"
      )
    );

    HttpClientResponseException e = assertThrows(
      HttpClientResponseException.class,
      () -> client.toBlocking().retrieve(
        HttpRequest.POST("/urls/shorten", requestBody).header("Authorization", "bad-token")
      )
    );

    assertEquals(401, e.getStatus().getCode());
  }

  @Test
  public void shouldThrowErrorWithEmptyBody() {
    String requestBody = "";

    HttpClientResponseException e = assertThrows(
      HttpClientResponseException.class,
      () -> client.toBlocking().retrieve(
        HttpRequest.POST("/urls/shorten", requestBody)
          .header("Authorization", userToken)
      )
    );

    assertEquals(400, e.getStatus().getCode());
  }

  @Test
  public void shouldSaveValidUrl() {
    String requestBody = jsonTool.toJson(
      new ShortLink(
        null,
        null,
        "https://devblogs.microsoft.com/typescript/announcing-the-new-typescript-handbook/"
      )
    );

    String body = client.toBlocking().retrieve(
      HttpRequest.POST(
        "/urls/shorten",
        requestBody
      ).header("Authorization", userToken)
    );

    Object parsedBody = jsonTool.fromJson(body, Object.class);

    assertThat(parsedBody).hasFieldOrProperty("shortened_url");
  }

  @Test
  public void shouldNotSaveInvalidUrl() {
    String requestBody = jsonTool.toJson(
      new ShortLink(
        null,
        null,
        "ht/devblogs.microsoft.com/typescript/announcing-the-new-typescript-handbook/"
      )
    );

    HttpClientResponseException e = assertThrows(
      HttpClientResponseException.class,
      () -> client.toBlocking().retrieve(
      HttpRequest.POST(
        "/urls/shorten",
        requestBody
      ).header("Authorization", userToken)
      )
    );

    assertEquals(400, e.getStatus().getCode());
  }

  @Test
  public void shouldSaveWithAlias() {
    String requestBody = jsonTool.toJson(
      new ShortLink(
        "tsBook",
        null,
        "https://devblogs.microsoft.com/typescript/announcing-the-new-typescript-handbook/"
      )
    );

    String responseBody = client.toBlocking().retrieve(
      HttpRequest.POST(
        "/urls/shorten",
        requestBody
      ).header("Authorization", userToken)
    );

    assertThat(getShortenedUrlFromResponseBody(responseBody)).isEqualTo("http://localhost:8080/r/tsBook");
  }

  @Test
  public void shouldRetrieveListOfUserUrls() {
    ShortLink requestBody1 = new ShortLink(
      "alias",
      null,
      "https://devblogs.microsoft.com/typescript/announcing-the-new-typescript-handbook/"
    );
    ShortLink expectedResponseBody1 = new ShortLink(
      "alias",
      "user1@mail.com",
      "https://devblogs.microsoft.com/typescript/announcing-the-new-typescript-handbook/"
    );

    ShortLink requestBody2 = new ShortLink(
      "fb",
      null,
      "https://ficbook.net/readfic/9255279"
    );
    ShortLink requestBody3 = new ShortLink(
      null,
      null,
      "https://uk.wikipedia.org/wiki/Замикання_(програмування)"
    );

    client.toBlocking().retrieve(
      HttpRequest.POST(
        "/urls/shorten",
        jsonTool.toJson(requestBody1)
      ).header("Authorization", userToken)
    );
    client.toBlocking().retrieve(
      HttpRequest.POST(
        "/urls/shorten",
        jsonTool.toJson(requestBody2)
      ).header("Authorization", userToken)
    );
    client.toBlocking().retrieve(
      HttpRequest.POST(
        "/urls/shorten",
        jsonTool.toJson(requestBody3)
      ).header("Authorization", userToken)
    );

    String responseBody = client.toBlocking().retrieve(
      HttpRequest.GET(
        "/urls"
      ).header("Authorization", userToken)
    );

    assertDoesNotThrow(() -> {
      LinksOfUser parsedResponse = jsonTool.fromJson(responseBody, LinksOfUser.class);

      assertEquals(3, parsedResponse.urls().size());
      assertThat(parsedResponse.urls()).asList().contains(expectedResponseBody1);
    });
  }

  @Test
  public void shouldBeAbleToDeleteAlias() {
    String myAlias = "sth";
    String requestBody = jsonTool.toJson(
      new ShortLink(
        myAlias,
        null,
        "https://ficbook.net/readfic/10022457/25815002?tab=1#tabContent"
      )
    );

    client.toBlocking().retrieve(
      HttpRequest.POST(
        "/urls/shorten",
        requestBody
      ).header("Authorization", userToken)
    );

    // the exception is always thrown, because the DELETE /urls/{alias} response has empty body
    HttpClientResponseException e = assertThrows(
      HttpClientResponseException.class,
      () -> client.toBlocking().retrieve(
        HttpRequest.DELETE("/urls/" + myAlias)
          .header("Authorization", userToken)
      )
    );
    assertEquals(204, e.getStatus().getCode());
  }

  @Test
  public void shouldNotBeAbleToDeleteNonExistentAlias() {
    String nonExistentAlias = "nonExistentAlias";

    HttpClientResponseException e = assertThrows(
      HttpClientResponseException.class,
      () -> client.toBlocking().retrieve(
        HttpRequest.DELETE("/urls/" + nonExistentAlias)
          .header("Authorization", userToken)
      )
    );
    assertEquals(404, e.getStatus().getCode());
  }
}
