package edu.kpi.testcourse.logic;

import java.net.URL;

/**
 * Mock data for short link record in the data storage.
 */
public class ShortLinkMock {
  private String shortLink;
  private String email;
  private URL destination;


  public ShortLinkMock(String shortLink, String email, URL destination) {
    this.shortLink = shortLink;
    this.email = email;
    this.destination = destination;
  }

  public String shortLink() {
    return shortLink;
  }

  public String userEmail() {
    return email;
  }

  public URL destination() {
    return destination;
  }
}
