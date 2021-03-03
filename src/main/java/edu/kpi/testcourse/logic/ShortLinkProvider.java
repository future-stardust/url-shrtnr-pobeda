package edu.kpi.testcourse.logic;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import javax.inject.Singleton;


/**
 * Business-logic class to provide read-write access to short links.
 * Currently uses mock data
 */
@Singleton
public class ShortLinkProvider {
  private ArrayList<ShortLinkMock> mockData;

  {
    try {
      mockData = new ArrayList<>(
        Arrays.asList(
          new ShortLinkMock(
              "t5H9678F",
             "user1@mail.com",
              new URL("https://www.texty.org.ua/")
          ),
          new ShortLinkMock(
              "f7G91057",
              "user1@mail.com",
              new URL("https://www.orthodoxum.org/")  // invalid URL
          ),
          new ShortLinkMock(
              "mpP76027",
              "user2@mail.com",
              new URL("https://uk.wikipedia.org/wiki/Ніїґата")
          ),
          new ShortLinkMock(
              "manga-character",
              "user1@mail.com",
              new URL("https://readmanga.live/klinok__rassekaiuchii_demonov/vol23/206#page=4")
          ),
          new ShortLinkMock(
              "19l9fr23",
              "user3@mail.com",
              new URL("https://darcs.realworldhaskell.org/static/00book.pdf")
          )
        )
      );
    } catch (MalformedURLException e) {
      e.printStackTrace();
    }
  }

  /**
   * Retrieve "long version" of link by short link.
   *
   * @param shortLink short link
   * @return Optional - "long link" if it had been found in the storage
   */
  public Optional<URL> getDestinationByShortLink(String shortLink) {
    for (ShortLinkMock link : mockData) {
      if (link.shortLink().equals(shortLink)) {
        return Optional.of(link.destination());
      }
    }
    return Optional.empty();
  }
}
