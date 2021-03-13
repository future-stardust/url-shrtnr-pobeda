package edu.kpi.testcourse.dto;

import io.micronaut.core.annotation.Introspected;
import java.util.ArrayList;

/**
 * DTO for urls of user in GET /urls.
 */
@Introspected
public final class LinksOfUser {
  private final ArrayList<ShortLink> urls;

  /**
   * Default constructor.
   *
   * @param urls ArrayList of ShortLink
   */
  public LinksOfUser(ArrayList<ShortLink> urls) {
    this.urls = urls;
  }

  /**
   * Getter for urls of user.
   *
   * @return urls
   */
  public ArrayList<ShortLink> urls() {
    return this.urls;
  }
}
