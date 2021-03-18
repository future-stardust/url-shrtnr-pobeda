package edu.kpi.testcourse.bigtable;

import edu.kpi.testcourse.dto.ShortLink;
import edu.kpi.testcourse.dto.User;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

/**
 * Interface for facade of BigTable.
 * Provides more high level operations using BigTable.
 */
public interface BigTableManager {
  /**
   * Finds user in stored data.
   *
   * @param email to search
   * @return Optional of User
   * @throws IOException when email hasn't found
   */
  Optional<User> findEmail(String email) throws IOException;

  /**
   * Persists provided user's data.
   *
   * @param user to save
   * @throws IOException when provided user already exists
   */
  void storeUser(User user) throws IOException;

  /**
   * Finds object in storage for specified alias if such exists.
   *
   * @param shortLink to find
   * @return ShortLink
   * @throws IOException if alias not found in storage
   */
  Optional<ShortLink> findShortLink(String shortLink) throws IOException;

  /**
   * Deletes specified alias.
   *
   * @param shortLink to delete
   * @throws IOException if specified alias is not exist
   */
  void deleteLink(String shortLink) throws IOException;

  /**
   * Searches all links storage for all aliases of given user.
   *
   * @param email of user
   * @return list of ShortLink
   * @throws IOException if there is no such user
   */
  ArrayList<ShortLink> listAllUserLinks(String email) throws IOException;

  /**
   * Persists link.
   *
   * @param link to store
   * @throws IOException if given link already exists
   */
  void storeLink(ShortLink link) throws IOException;
}
