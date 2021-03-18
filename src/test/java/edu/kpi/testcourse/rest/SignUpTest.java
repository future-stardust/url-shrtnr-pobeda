package edu.kpi.testcourse.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import edu.kpi.testcourse.dto.User;
import edu.kpi.testcourse.helper.JsonTool;
import io.micronaut.context.ApplicationContext;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import javax.inject.Inject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

@MicronautTest
public class SignUpTest {
  @Inject
  JsonTool jsonTool;

  private static EmbeddedServer server;
  private static HttpClient client;

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
  public void shouldBeAbleToSaveUserWithCorrectData() {
    User correctUser = new User("someGoodUser@somemail.net", "pwd33");

    // empty response body is viewed as exception by Micronaut
    HttpClientResponseException e = assertThrows(
      HttpClientResponseException.class,
      () -> client.toBlocking().retrieve(
          HttpRequest.POST(
            "/users/signup",
            jsonTool.toJson(correctUser)
          )
        )
    );

    assertEquals(201, e.getStatus().getCode());
  }

  @Test
  public void shouldNotBeAbleToSaveUserWithIncorrectData() {
    User badUser = new User("badUser@somemail.ru", "");

    // empty response body is viewed as exception by Micronaut
    HttpClientResponseException e = assertThrows(
      HttpClientResponseException.class,
      () -> client.toBlocking().retrieve(
        HttpRequest.POST(
          "/users/signup",
          jsonTool.toJson(badUser)
        )
      )
    );

    assertEquals(400, e.getStatus().getCode());
    assertEquals(
      "Failed to sign up: email or password were not provided",
      e.getResponse().body()
    );
  }

  @Test
  public void shouldNotBeAbleToSaveTheSameUser() {
    String duplicateEmail = "TheBestCharacter012@mail.com";

    User goodUser1 = new User(duplicateEmail, "t5TLg13wll__4");
    User goodUser2 = new User(duplicateEmail, "4");

    assertThrows(
      HttpClientResponseException.class,
      () -> client.toBlocking().retrieve(
        HttpRequest.POST(
          "/users/signup",
          jsonTool.toJson(goodUser1)
        )
      )
    );

    HttpClientResponseException e = assertThrows(
      HttpClientResponseException.class,
      () -> client.toBlocking().retrieve(
        HttpRequest.POST(
          "/users/signup",
          jsonTool.toJson(goodUser2)
        )
      )
    );

    assertEquals(400, e.getStatus().getCode());
    assertEquals(
      "This user already exists",
      e.getResponse().body()
    );
  }
}
