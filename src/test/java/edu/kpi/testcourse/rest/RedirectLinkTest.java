package edu.kpi.testcourse.rest;

import com.google.gson.Gson;
import edu.kpi.testcourse.dto.ShortLink;
import io.micronaut.context.ApplicationContext;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.runtime.server.EmbeddedServer;
import java.net.URL;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RedirectLinkTest {
  private static final String TEST_VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJJc3N1ZXIiOiJ1c2VyMUBtYWlsLmNvbSIsImV4cCI6MTY0NjczMDczOCwia"
    + "WF0IjoxNjE1MTk0NzM4fQ.SYb7CJl3Gx0AyeHcRGR6jWr6Gbxg0m8b7V2ZhynrYuY";
  private static final String TEST_VALID_TOKEN2 = "eyJhbGciOiJIUzI1NiJ9.eyJJc3N1ZXIiOiJ1c2VyMkBtYWlsLmNvbSIsImV4cCI6MTY0NjczMDczOCwia"
    + "WF0IjoxNjE1MTk0NzM4fQ.1VyiEw77yt998_6zNp-fxSMwpMyY93beRMMLno_uKSg";

  private static String randomAlias;
  private static String customAlias;

  private static EmbeddedServer server;
  private static HttpClient client;
  private static final Gson g = new Gson();

  /* @Introspected
  private static class ShortenedUrlResponse {
    public String shortened_url;
  }*/

  private static String getShortenedUrlFromResponseBody(String body) {
    ShortLink parsed = g.fromJson(body, ShortLink.class);
    return parsed.alias();
  }

  @Test
  public void setupServer() {
    server = ApplicationContext.run(EmbeddedServer.class);
    client = server
      .getApplicationContext()
      .createBean(HttpClient.class, server.getURL());

    assertDoesNotThrow(() -> {
      URL url1 = new URL("https://devblogs.microsoft.com/typescript/announcing-the-new-typescript-handbook/");
      URL url2 = new URL("https://darcs.realworldhaskell.org/static/00book.pdf");

      System.out.println(url1);

      String noAliasBody = client.toBlocking().retrieve(
        HttpRequest.POST("/urls/shorten", new ShortLink(
          null,
          null,
          url1
        )).header("token", TEST_VALID_TOKEN)
      );
      /* String withAliasBody = client.toBlocking().retrieve(
        HttpRequest.POST("/urls/shorten", new ShortLink(
          "haskell",
          "user1@mail.com",
          url2
        )).header("token", TEST_VALID_TOKEN)
      );

      randomAlias = getShortenedUrlFromResponseBody(noAliasBody);
      customAlias = getShortenedUrlFromResponseBody(withAliasBody);*/
    });
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
  public void registeredShouldRedirect() {
    /* assertDoesNotThrow(() -> {
      String withAliasBody2 = client.toBlocking().retrieve(
        HttpRequest.POST("/urls/shorten", new ShortLink(
          "haskell2",
          "user1@mail.com",
          new URL("https://darcs.realworldhaskell.org/static/00book.pdf")
        )).header("token", TEST_VALID_TOKEN)
      );
      String customAlias2 = getShortenedUrlFromResponseBody(withAliasBody2);

    String responseBody = client.toBlocking()
      .retrieve(
        HttpRequest.GET("/r/haskell2")
          .header("token", TEST_VALID_TOKEN)
      );*/
    // });
  }

  @Test
  public void unregisteredShouldRedirect() {
    /* String responseBody = client.toBlocking()
      .retrieve(
        HttpRequest.GET("/r/haskell")
        // .header("token", TEST_VALID_TOKEN)
      );

    System.out.println(responseBody);*/
  }

  @Test
  public void shouldNotRedirectByNonexistentLink() {

  }

  @Test
  public void shouldNotRedirectByDeletedLink() {

  }
}
