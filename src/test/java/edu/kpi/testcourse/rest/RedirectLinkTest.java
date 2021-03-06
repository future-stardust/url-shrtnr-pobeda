package edu.kpi.testcourse.rest;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import org.junit.jupiter.api.function.Executable;
import javax.inject.Inject;
import org.junit.jupiter.api.Test;

public class RedirectLinkTest {

  @Inject
  @Client("/")
  RxHttpClient client;

  @Test
  void shouldReceiveResponse() {
    Executable e = () ->
      client.toBlocking().exchange(HttpRequest.GET("/").accept(MediaType.TEXT_PLAIN));
  }
}
