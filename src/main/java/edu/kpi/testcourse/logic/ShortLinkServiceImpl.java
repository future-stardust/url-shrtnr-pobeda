package edu.kpi.testcourse.logic;

import edu.kpi.testcourse.dto.ShortLink;
import edu.kpi.testcourse.exception.url.InvalidUrlException;
import edu.kpi.testcourse.repository.LinkRepository;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Business-logic class to provide read-write access to short links.
 * Currently uses mock data
 */
@Singleton
public class ShortLinkServiceImpl implements ShortLinkService {

  public static final int STANDARD_SHORT_LINK_LENGTH = 8;
  public static final String SHORT_LINK_BEGINNING = "http://localhost:8080/r/";
  public static final String ALIAS_PATTERN = "[0-9a-zA-Z]+";

  @Inject
  private LinkRepository linkRepo;

  /**
   * Retrieve "long version" of link by short link.
   *
   * @param shortLink short link
   * @return Optional - "long link" if it had been found in the storage
   */
  @Override
  public Optional<String> getDestinationByShortLink(String shortLink) {
    Optional<ShortLink> resp = linkRepo.findByShortLink(shortLink);
    return resp.isPresent()
       ? Optional.of(resp.get().url())
       : Optional.empty();
  }

  /**
   * Delete link entity by alias if belongs to a user with given email.
   *
   * @param email user's email
   * @param shortLink link's alias
   * @return if a link has been returned
   */
  @Override
  public boolean deleteLinkIfBelongsToUser(String email, String shortLink) {
    return linkRepo.deleteLink(email, shortLink);
  }

  /**
   * Get links created by a user.
   *
   * @param email email of user
   * @return list of user's links
   */
  @Override
  public ArrayList<ShortLink> getLinksByUserEmail(String email) {
    return linkRepo.getLinksOfUser(email);
  }

  /**
   * Validate a link alias proposed by user. Must be not present.
   *
   * @param alias short link provided by a user
   * @return is alias already used
   */
  private boolean isAliasAlreadyUsed(String alias) {
    return this.getDestinationByShortLink(alias).isPresent();
  }

  /**
   * Validate a link alias proposed by user. Must be an alphanumeric string.
   *
   * @param alias short link provided by a user
   * @return does alias matches regex [0-9a-zA-Z]+
   */
  private boolean isAliasAlphanumeric(String alias) {
    return alias.matches(ALIAS_PATTERN);
  }

  /**
   * Randomly generate alias for long link.
   *
   * @return alias - 8-digits alphanumeric sequence consisting of characters [0-9a-zA-Z]
   */
  @Override
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
   * Check validity of URL.
   *
   * @param destination URL that should be validated
   * @return is a 'destination' parameter a valid URL
   */
  @Override
  public boolean isUrlValid(String destination) {
    try {
      new URL(destination).toURI();
      return true;
    } catch (MalformedURLException | URISyntaxException e) {
      return false;
    }
  }

  /**
   * Append alias to server address.
   *
   * @param shortLink - alias.
   * @return full link.
   */
  public static String createFullLink(String shortLink) {
    return SHORT_LINK_BEGINNING + shortLink;
  }

  /**
   * Create and save user link without custom alias provided.
   * Generates alias automatically.
   *
   * @param destination - "long" link alias must be provided for
   * @return a link that has been created
   */
  @Override
  public ShortLink saveLink(String userEmail, String destination)
      throws InvalidUrlException {

    if (this.isUrlValid(destination)) {
      String alias = this.generateAlias();
      while (this.getDestinationByShortLink(alias).isPresent()) {
        alias = this.generateAlias();
      }
      ShortLink link = new ShortLink(alias, userEmail, destination);

      linkRepo.saveLink(link);

      return link;
    } else {
      throw new InvalidUrlException("Provided url is not valid http or https url");
    }
  }

  /**
   * Create and save user link with custom alias provided.
   *
   * @param destination - "long" link alias must be provided for
   * @param alias - custom user alias for a "long" link
   * @return a link that has been created
   */
  @Override
  public ShortLink saveLink(String userEmail, String destination, String alias)
      throws InvalidUrlException {

    if (!this.isUrlValid(destination)) {
      throw new InvalidUrlException("Provided url is not valid http or https url");
    } else if (this.isAliasAlreadyUsed(alias)) {
      throw new InvalidUrlException("Desired alias is already taken");
    } else if (!this.isAliasAlphanumeric(alias)) {
      throw new InvalidUrlException("Desired alias is not alphanumeric string");
    } else {
      ShortLink link = new ShortLink(alias, userEmail, destination);

      linkRepo.saveLink(link);

      return link;
    }
  }
}
