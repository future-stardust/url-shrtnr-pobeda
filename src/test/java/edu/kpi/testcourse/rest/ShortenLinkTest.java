package edu.kpi.testcourse.rest;

import com.google.gson.Gson;
import edu.kpi.testcourse.rest.UrlController.UserUrl;
import io.micronaut.context.ApplicationContext;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.runtime.server.EmbeddedServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ShortenLinkTest {

  private static final String TEST_VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJJc3N1ZXIiOiJ1c2VyMUBtYWlsLmNvbSIsImV4cCI6MTY0NjczMDczOCwia"
    + "WF0IjoxNjE1MTk0NzM4fQ.SYb7CJl3Gx0AyeHcRGR6jWr6Gbxg0m8b7V2ZhynrYuY";
  private static final String TEST_VALID_TOKEN2 = "eyJhbGciOiJIUzI1NiJ9.eyJJc3N1ZXIiOiJ1c2VyMkBtYWlsLmNvbSIsImV4cCI6MTY0NjczMDczOCwia"
    + "WF0IjoxNjE1MTk0NzM4fQ.1VyiEw77yt998_6zNp-fxSMwpMyY93beRMMLno_uKSg";

  private static EmbeddedServer server;
  private static HttpClient client;
  private static final Gson g = new Gson();

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
    HttpClientResponseException e = assertThrows(
      HttpClientResponseException.class,
      () -> client.toBlocking().retrieve(
        HttpRequest.POST("/urls/shorten", "")
      )
    );

    assertEquals(400, e.getStatus().getCode());
  }

  @Test
  public void shouldThrowErrorWithBadToken() {
    HttpClientResponseException e = assertThrows(
      HttpClientResponseException.class,
      () -> client.toBlocking().retrieve(
        HttpRequest.POST("/urls/shorten", "").header("token", "bad-token")
      )
    );

    assertEquals(400, e.getStatus().getCode());
  }

  @Test
  public void shouldThrowErrorWithEmptyBody() {
    HttpClientResponseException e = assertThrows(
      HttpClientResponseException.class,
      () -> client.toBlocking().retrieve(
        HttpRequest.POST("/urls/shorten", "").header("token", TEST_VALID_TOKEN)
      )
    );

    assertEquals(400, e.getStatus().getCode());
  }

  @Test
  public void shouldSaveValidUrl() {
    String body = client.toBlocking().retrieve(
      HttpRequest.POST("/urls/shorten", new UserUrl(
        "https://devblogs.microsoft.com/typescript/announcing-the-new-typescript-handbook/",
        null
      )).header("token", TEST_VALID_TOKEN)
    );

    Object parsedBody = g.fromJson(body, Object.class);

    assertThat(parsedBody).hasFieldOrProperty("shortened_url");
  }

  @Test
  public void shouldNotSaveInvalidUrl() {
    HttpClientResponseException e = assertThrows(
      HttpClientResponseException.class,
      () -> client.toBlocking().retrieve(
      HttpRequest.POST("/urls/shorten", new UserUrl(
        "ht/devblogs.microsoft.com/typescript/announcing-the-new-typescript-handbook/",
        null
      )).header("token", TEST_VALID_TOKEN)
      )
    );

    assertEquals(400, e.getStatus().getCode());
  }

  @Test
  public void shouldSaveWithAlias() {
    String alias = "alias";

    String body = client.toBlocking().retrieve(
      HttpRequest.POST("/urls/shorten", new UserUrl(
        "https://devblogs.microsoft.com/typescript/announcing-the-new-typescript-handbook/",
        alias
      )).header("token", TEST_VALID_TOKEN)
    );

    assertThat(body.contains("\"shortened_url\":\"http://localhost:8080/r/alias\"")).isEqualTo(true);
  }
}
