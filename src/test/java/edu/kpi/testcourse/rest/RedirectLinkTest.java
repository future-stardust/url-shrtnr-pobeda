package edu.kpi.testcourse.rest;

import com.google.gson.Gson;
import edu.kpi.testcourse.rest.UrlController.UserUrl;
import io.micronaut.context.ApplicationContext;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.runtime.server.EmbeddedServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

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

  @Introspected
  private static class ShortenedUrlResponse {
    public String shortened_url;
  }

  private static String getShortenedUrlFromResponseBody(String body) {
    ShortenedUrlResponse parsed = g.fromJson(body, ShortenedUrlResponse.class);
    return parsed.shortened_url;
  }

  @BeforeAll
  public static void setupServer() {
    server = ApplicationContext.run(EmbeddedServer.class);
    client = server
      .getApplicationContext()
      .createBean(HttpClient.class, server.getURL());

    String noAliasBody = client.toBlocking().retrieve(
      HttpRequest.POST("/urls/shorten", new UserUrl(
        "https://devblogs.microsoft.com/typescript/announcing-the-new-typescript-handbook/",
        null
      )).header("token", TEST_VALID_TOKEN)
    );
    String withAliasBody = client.toBlocking().retrieve(
      HttpRequest.POST("/urls/shorten", new UserUrl(
        "https://darcs.realworldhaskell.org/static/00book.pdf",
        "haskell"
      )).header("token", TEST_VALID_TOKEN)
    );

    randomAlias = getShortenedUrlFromResponseBody(noAliasBody);
    customAlias = getShortenedUrlFromResponseBody(withAliasBody);
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
    String withAliasBody2 = client.toBlocking().retrieve(
      HttpRequest.POST("/urls/shorten", new UserUrl(
        "https://darcs.realworldhaskell.org/static/00book.pdf",
        "haskell2"
      )).header("token", TEST_VALID_TOKEN)
    );
    String customAlias2 = getShortenedUrlFromResponseBody(withAliasBody2);

    /* String responseBody = client.toBlocking()
      .retrieve(
        HttpRequest.GET("/r/haskell2")
          .header("token", TEST_VALID_TOKEN)
      );*/
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
