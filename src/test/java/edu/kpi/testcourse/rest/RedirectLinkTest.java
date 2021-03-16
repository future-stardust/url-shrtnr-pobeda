package edu.kpi.testcourse.rest;

import com.google.gson.Gson;
import edu.kpi.testcourse.dto.ShortLink;
import io.micronaut.context.ApplicationContext;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import java.util.TreeMap;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

@MicronautTest
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

  /**
   * Get shortened URL from POST /urls/shorten response.
   *
   * @param body { "shortened_url": "localhost:8080/r/4rT7uu6Y }
   * @return the value of "shortened_url" field
   */
  private static String getShortenedUrlFromResponseBody(String body) {
    TreeMap<String, String> parsed = g.fromJson(body, TreeMap.class);
    return parsed.get("shortened_url");
  }

  @BeforeAll
  public static void setupServer() {
    server = ApplicationContext.run(EmbeddedServer.class);
    client = server
      .getApplicationContext()
      .createBean(HttpClient.class, server.getURL());

    String url1 = "https://devblogs.microsoft.com/typescript/announcing-the-new-typescript-handbook/";
    String url2 = "https://darcs.realworldhaskell.org/static/00book.pdf";

    String noAliasBody = client.toBlocking().retrieve(
      HttpRequest.POST(
        "/urls/shorten",
        g.toJson(
          new ShortLink(
            null,
            null,
            url1
          )
        )
      ).header("token", TEST_VALID_TOKEN)
    );
    String withAliasBody = client.toBlocking().retrieve(
      HttpRequest.POST(
        "/urls/shorten",
        g.toJson(
          new ShortLink(
            "haskell",
            "user1@mail.com",
            url2
          )
        )
      ).header("token", TEST_VALID_TOKEN)
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
      HttpRequest.POST(
        "/urls/shorten",
        g.toJson(
          new ShortLink(
            "haskell2",
            "user1@mail.com",
            "https://darcs.realworldhaskell.org/static/00book.pdf"
          )
        )
      ).header("token", TEST_VALID_TOKEN)
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
