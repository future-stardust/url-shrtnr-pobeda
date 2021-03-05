package edu.kpi.testcourse.logic;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;
import javax.inject.Singleton;

/**
 * Business-logic class to provide read-write access to short links.
 * Currently uses mock data
 */
@Singleton
public class ShortLinkServiceImpl implements ShortLinkService {

  public static final int STANDARD_SHORT_LINK_LENGTH = 8;
  public static final String ALIAS_PATTERN = "[0-9a-zA-Z]+";

  private final ArrayList<ShortLinkMock> mockData = new ArrayList<>();

  /**
   * Retrieve "long version" of link by short link.
   *
   * @param shortLink short link
   * @return Optional - "long link" if it had been found in the storage
   */
  public Optional<URL> getDestinationByShortLink(String shortLink) {
    /*
    Temporary implementation.
    Future correct implementation:
    return this.linkRepo.findByShortLink(shortLink).destination;
     */
    for (ShortLinkMock link : mockData) {
      // System.out.println(link);
      if (link.shortLink().equals(shortLink)) {
        return Optional.of(link.destination());
      }
    }
    return Optional.empty();
  }

  /**
   * Validate a link alias proposed by user.
   * Short link should be an alphanumeric string and not exist in the storage before.
   *
   * @param shortLink link proposed by a user
   * @return bool - is a link valid
   */
  private boolean isAliasValid(String shortLink) {
    boolean doesNotExistInStorage = this.getDestinationByShortLink(shortLink).isEmpty();

    return shortLink.matches(ALIAS_PATTERN) && doesNotExistInStorage;
  }

  /**
   * Randomly generate alias for long link.
   *
   * @return alias - 8-digits alphanumeric sequence consisting of characters [0-9a-zA-Z]
   */
  public String generateAlias() {
    Random random = new Random();

    int minNumCode = '0';
    int maxNumCode = '9';
    int minBigLetterCode = 'A';
    int maxBigLetterCode = 'Z';
    int minSmallLetterCode = 'a';
    int maxSmallLetterCode = 'z';

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
   * Create an URL if it's valid.
   *
   * @param destination URL that should be validated
   * @return URL if it can be created
   */
  public Optional<URL> safelyCreateUrl(String destination) {
    try {
      URL url = new URL(destination);
      return Optional.of(url);
    } catch (MalformedURLException e) {
      return Optional.empty();
    }
  }

  /**
   * Create and save user link without custom alias provided.
   * Generates alias automatically.
   *
   * @param destination - "long" link alias must be provided for
   * @return if a link has been created
   */
  public Optional<ShortLinkMock> saveLink(String userEmail, String destination) {
    Optional<URL> destinationLink = this.safelyCreateUrl(destination);

    if (destinationLink.isPresent()) {
      String alias = this.generateAlias();
      while (this.getDestinationByShortLink(alias).isPresent()) {
        alias = this.generateAlias();
      }
      ShortLinkMock link = new ShortLinkMock(alias, userEmail, destinationLink.get());
      // temporary implementation
      this.mockData.add(link);

      return Optional.of(link);
    }

    return Optional.empty();
  }

  /**
   * Create and save user link with custom alias provided.
   *
   * @param destination - "long" link alias must be provided for
   * @param alias - custom user alias for a "long" link
   * @return if a link has been created
   */
  public Optional<ShortLinkMock> saveLink(String userEmail, String destination, String alias) {
    Optional<URL> destinationLink = this.safelyCreateUrl(destination);

    if (destinationLink.isPresent() && this.isAliasValid(alias)) {
      ShortLinkMock link = new ShortLinkMock(alias, userEmail, destinationLink.get());
      // temporary implementation
      this.mockData.add(link);

      return Optional.of(link);
    }

    return Optional.empty();
  }
}
