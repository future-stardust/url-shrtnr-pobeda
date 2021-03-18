package edu.kpi.testcourse.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import edu.kpi.testcourse.dto.ShortLink;
import edu.kpi.testcourse.helper.JsonToolJacksonImpl;
import io.micronaut.context.ApplicationContext;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.reactivex.subscribers.TestSubscriber;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Publisher;

@MicronautTest
public class RedirectLinkTest {

  private static final String TEST_VALID_TOKEN =
    "eyJhbGciOiJIUzI1NiJ9.eyJJc3N1ZXIiOiJ1c2VyMUBtYWlsLmNvbSIsImV4cCI6MTY0NjczMDczOCwia"
      + "WF0IjoxNjE1MTk0NzM4fQ.SYb7CJl3Gx0AyeHcRGR6jWr6Gbxg0m8b7V2ZhynrYuY";
  private static final String TEST_VALID_TOKEN2 =
    "eyJhbGciOiJIUzI1NiJ9.eyJJc3N1ZXIiOiJ1c2VyMkBtYWlsLmNvbSIsImV4cCI6MTY0NjczMDczOCwia"
      + "WF0IjoxNjE1MTk0NzM4fQ.1VyiEw77yt998_6zNp-fxSMwpMyY93beRMMLno_uKSg";

  private static String randomAlias;
  private static String customAlias;

  private static EmbeddedServer server;
  private static HttpClient client;

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
    // as jsonTool can be injected but cannot be retrieved in static context,
    // create it as local field.
    JsonToolJacksonImpl jsonTool = new JsonToolJacksonImpl();

    server = ApplicationContext.run(EmbeddedServer.class);
    client = server
      .getApplicationContext()
      .createBean(HttpClient.class, server.getURL());

    String url1 = "https://devblogs.microsoft.com/typescript/announcing-the-new-typescript-handbook/";
    String url2 = "https://darcs.realworldhaskell.org/static/00book.pdf";

    String noAliasBody = client.toBlocking().retrieve(
      HttpRequest.POST(
        "/urls/shorten",
        jsonTool.toJson(
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
        jsonTool.toJson(
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
    // Micronaut seems to have different approach in testing redirect responses
    // then the Publisher/Subscriber contract is used in the test
    Publisher<HttpResponse<Object>> exchange = client.exchange(
      HttpRequest.GET("/r/haskell")
        .header("token", TEST_VALID_TOKEN)
        .contentType(MediaType.APPLICATION_FORM_URLENCODED), Object.class
    );

    TestSubscriber<HttpResponse<?>> testSubscriber = new TestSubscriber<>() {
      @Override
      public void onNext(HttpResponse<?> httpResponse) {
        assertNotNull(httpResponse);
        assertEquals(HttpStatus.MOVED_PERMANENTLY, httpResponse.status());
      }
    };

    exchange.subscribe(testSubscriber);

    // await to allow for response
    testSubscriber.awaitTerminalEvent(2, TimeUnit.SECONDS);
  }

  @Test
  public void unregisteredShouldRedirect() {
    // Micronaut seems to have different approach in testing redirect responses
    // then the Publisher/Subscriber contract is used in the test
    Publisher<HttpResponse<Object>> exchange = client.exchange(
      HttpRequest.GET("/r/haskell")
        .header("token", TEST_VALID_TOKEN)
        .contentType(MediaType.APPLICATION_FORM_URLENCODED), Object.class
    );

    TestSubscriber<HttpResponse<?>> testSubscriber = new TestSubscriber<>() {
      @Override
      public void onNext(HttpResponse<?> httpResponse) {
        assertNotNull(httpResponse);
        assertEquals(HttpStatus.MOVED_PERMANENTLY, httpResponse.status());
      }
    };

    exchange.subscribe(testSubscriber);

    // await to allow for response
    testSubscriber.awaitTerminalEvent(2, TimeUnit.SECONDS);
  }

  @Test
  public void shouldNotRedirectByNonexistentLink() {
    HttpClientResponseException e = assertThrows(
      HttpClientResponseException.class,
      () -> client.toBlocking().retrieve(
        HttpRequest.GET("/r/nonExistentAlias")
      )
    );

    assertEquals(404, e.getStatus().getCode());
  }

  @Test
  public void shouldNotRedirectByDeletedLink() {
    // retrieve alias from "http://localhost:8080/r/{alias}"
    String[] shortLinkParts = randomAlias.split("/");
    String aliasToDelete = shortLinkParts[shortLinkParts.length-1];

    // the exception is always thrown, because the DELETE /urls/{alias} response has empty body
    assertThrows(
      HttpClientResponseException.class,
      () -> client.toBlocking().retrieve(
        HttpRequest.DELETE("/urls/" + aliasToDelete).header("token", TEST_VALID_TOKEN)
      )
    );

    HttpClientResponseException e = assertThrows(
      HttpClientResponseException.class,
      () -> client.toBlocking().retrieve(
        HttpRequest.GET("/r/" + aliasToDelete)
      )
    );

    assertEquals(404, e.getStatus().getCode());
  }
}
