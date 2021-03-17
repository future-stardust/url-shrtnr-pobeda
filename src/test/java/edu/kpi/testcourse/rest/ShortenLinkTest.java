package edu.kpi.testcourse.rest;

import edu.kpi.testcourse.dto.LinksOfUser;
import edu.kpi.testcourse.dto.ShortLink;
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

  private static final String TEST_VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJJc3N1ZXIiOiJ1c2VyMUBtYWlsLmNvbSIsImV4cCI6MTY0NjczMDczOCwia"
    + "WF0IjoxNjE1MTk0NzM4fQ.SYb7CJl3Gx0AyeHcRGR6jWr6Gbxg0m8b7V2ZhynrYuY";
  private static final String TEST_VALID_TOKEN2 = "eyJhbGciOiJIUzI1NiJ9.eyJJc3N1ZXIiOiJ1c2VyMkBtYWlsLmNvbSIsImV4cCI6MTY0NjczMDczOCwia"
    + "WF0IjoxNjE1MTk0NzM4fQ.1VyiEw77yt998_6zNp-fxSMwpMyY93beRMMLno_uKSg";
  private static final String userEmail = "user1@mail.com";

  private static EmbeddedServer server;
  private static HttpClient client;

  @Inject
  JsonToolJacksonImpl jsonTool;

  /**
   * Get shortened URL from POST /urls/shorten response.
   *
   * @param body { "shortened_url": "localhost:8080/r/4rT7uu6Y }
   * @return the value of "shortened_url" field
   */
  private static String getShortenedUrlFromResponseBody(String body) {
    JsonToolJacksonImpl jsonTool = new JsonToolJacksonImpl();

    TreeMap<String, String> parsed = jsonTool.fromJson(body, TreeMap.class);
    return parsed.get("shortened_url");
  }

  @BeforeAll
  public static void setupServer() {
    server = ApplicationContext.run(EmbeddedServer.class);
    client = server
      .getApplicationContext()
      .createBean(HttpClient.class, server.getURL());
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

    assertEquals(400, e.getStatus().getCode());
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
        HttpRequest.POST("/urls/shorten", requestBody).header("token", "bad-token")
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
        HttpRequest.POST("/urls/shorten", requestBody).header("token", TEST_VALID_TOKEN)
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
      ).header("token", TEST_VALID_TOKEN)
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
      ).header("token", TEST_VALID_TOKEN)
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
      ).header("token", TEST_VALID_TOKEN)
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
      ).header("token", TEST_VALID_TOKEN)
    );
    client.toBlocking().retrieve(
      HttpRequest.POST(
        "/urls/shorten",
        jsonTool.toJson(requestBody2)
      ).header("token", TEST_VALID_TOKEN)
    );
    client.toBlocking().retrieve(
      HttpRequest.POST(
        "/urls/shorten",
        jsonTool.toJson(requestBody3)
      ).header("token", TEST_VALID_TOKEN)
    );

    String responseBody = client.toBlocking().retrieve(
      HttpRequest.GET(
        "/urls"
      ).header("token", TEST_VALID_TOKEN)
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
      ).header("token", TEST_VALID_TOKEN)
    );

    // the exception is always thrown, because the DELETE /urls/{alias} response has empty body
    HttpClientResponseException e = assertThrows(
      HttpClientResponseException.class,
      () -> client.toBlocking().retrieve(
        HttpRequest.DELETE("/urls/" + myAlias).header("token", TEST_VALID_TOKEN)
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
        HttpRequest.DELETE("/urls/" + nonExistentAlias).header("token", TEST_VALID_TOKEN)
      )
    );
    assertEquals(404, e.getStatus().getCode());
  }
}
