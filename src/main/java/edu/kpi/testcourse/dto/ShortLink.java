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
  private String url;

  /**
   * Default constructor.
   *
   * @param alias alias for a "long" link
   * @param email user's email
   * @param url link to shorten
   */
  public ShortLink(String alias, String email, String url) {
    this.alias = alias;
    this.email = email;
    this.url = url;
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
  public String url() {
    return url;
  }
}
