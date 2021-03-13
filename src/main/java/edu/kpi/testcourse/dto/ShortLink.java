package edu.kpi.testcourse.dto;

import io.micronaut.core.annotation.Introspected;
import java.net.URL;

/**
 * Mock data for short link record in the data storage.
 */
@Introspected
public final class ShortLink {
  private String alias;
  private String email;
  private URL url;

  /**
   * Default constructor.
   *
   * @param shortLink alias
   * @param email user's
   * @param destination llink
   */
  public ShortLink(String shortLink, String email, URL destination) {
    this.alias = shortLink;
    this.email = email;
    this.url = destination;
  }

  /**
   * Getter for alias.
   *
   * @return shortLink
   */
  public String alias() {
    return alias;
  }

  /**
   * Getter for user email.
   *
   * @return email
   */
  public String email() {
    return email;
  }

  /**
   * Getter for destination.
   *
   * @return destination
   */
  public URL url() {
    return url;
  }
}
