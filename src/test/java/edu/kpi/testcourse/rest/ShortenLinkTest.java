package edu.kpi.testcourse.rest;

import com.google.gson.Gson;
import edu.kpi.testcourse.dto.ShortLink;
import io.micronaut.context.ApplicationContext;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.stream.Stream;
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

    // clear all links in 'links' directory
    // temporary solution before we configure our data storage properly
    assertDoesNotThrow(() -> {
      Path linksPath = Paths.get("data/links/");

      try (Stream<Path> walk = Files.walk(linksPath)) {
        walk.sorted(Comparator.reverseOrder())
          .map(Path::toFile)
          .forEach(File::delete);
      }
    });
  }

  @Test
  public void shouldThrowErrorWithoutToken() {
    String requestBody = g.toJson(
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
    String requestBody = g.toJson(
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
    String requestBody = g.toJson(
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

    Object parsedBody = g.fromJson(body, Object.class);

    assertThat(parsedBody).hasFieldOrProperty("shortened_url");
  }

  @Test
  public void shouldNotSaveInvalidUrl() {
    String requestBody = g.toJson(
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
    String requestBody = g.toJson(
      new ShortLink(
        "alias",
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

    assertThat(body.contains("\"shortened_url\":\"http://localhost:8080/r/alias\"")).isEqualTo(true);
  }
}
