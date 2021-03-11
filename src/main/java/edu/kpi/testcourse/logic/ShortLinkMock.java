package edu.kpi.testcourse.logic;

import java.net.URL;

/**
 * Mock data for short link record in the data storage.
 */
public class ShortLinkMock {
  private String shortLink;
  private String email;
  private URL destination;

  /**
   * Default constructor.
   *
   * @param shortLink alias
   * @param email user's
   * @param destination llink
   */
  public ShortLinkMock(String shortLink, String email, URL destination) {
    this.shortLink = shortLink;
    this.email = email;
    this.destination = destination;
  }

  /**
   * Getter for alias.
   *
   * @return shortLink
   */
  public String shortLink() {
    return shortLink;
  }

  /**
   * Getter for user email.
   *
   * @return email
   */
  public String userEmail() {
    return email;
  }

  /**
   * Getter for destination.
   *
   * @return destination
   */
  public URL destination() {
    return destination;
  }
}
