package edu.kpi.testcourse.logic;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.inject.Singleton;

/**
 * Business-logic class to provide read-write access to short links.
 * Currently uses mock data
 */
@Singleton
public class ShortLinkProvider {

  private static final int STANDARD_SHORT_LINK_LENGTH = 8;
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
              "mc",
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
  public URL getDestinationByShortLink(String shortLink) {
    /*
    Future correct implementation:
    return this.linkRepo.findByShortLink(shortLink).destination;
     */
    for (ShortLinkMock link : mockData) {
      if (link.shortLink().equals(shortLink)) {
        return link.destination();
      }
    }
    return null;
  }

  /**
   * Validate a link alias proposed by user.
   * Short link should be an alphanumeric string and not exist in the storage before.
   *
   * @param shortLink link proposed by a user
   * @return bool - is a link valid
   */
  private boolean isLinkValid(String shortLink) {
    String pattern = "[0-9a-zA-Z]+";
    boolean doesNotExistInStorage = this.getDestinationByShortLink(shortLink) == null;

    return shortLink.matches(pattern) && doesNotExistInStorage;
  }

  /**
   * Randomly generate alias for long link.
   *
   * @return alias - 8-digits alphanumeric sequence consisting of characters [0-9a-zA-Z]
   */
  public String generateAlias() {
    Random random = new Random();

    int minNumCode = (int) '0';
    int maxNumCode = (int) '9';
    int minBigLetterCode = (int) 'A';
    int maxBigLetterCode = (int) 'Z';
    int minSmallLetterCode = (int) 'a';
    int maxSmallLetterCode = (int) 'z';

    return random.ints(minNumCode, maxSmallLetterCode + 1)
      .filter(i ->
        (i >= minNumCode && i <= maxNumCode)
        || (i >= minBigLetterCode && i <= maxBigLetterCode)
        || (i >= minSmallLetterCode && i <= maxSmallLetterCode)
      ).limit(STANDARD_SHORT_LINK_LENGTH)
      .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
      .toString();
  }

  /**
   * Create and save user link without custom alias provided.
   * Generates alias automatically.
   *
   * @param destination - "long" link alias must be provided for
   * @return if a link has been created
   */
  public ShortLinkMock saveLink(String destination) {
    return mockData.get(0);
  }

  /**
   * Create and save user link with custom alias provided.
   *
   * @param destination - "long" link alias must be provided for
   * @param alias - custom user alias for a "long" link
   * @return if a link has been created
   */
  public ShortLinkMock saveLink(String destination, String alias) {
    return mockData.get(0);
  }
}
