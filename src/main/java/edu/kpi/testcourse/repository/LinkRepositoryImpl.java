package edu.kpi.testcourse.repository;

import edu.kpi.testcourse.bigtable.BigTableManager;
import edu.kpi.testcourse.exception.bigtable.BigTableException;
import edu.kpi.testcourse.dto.ShortLink;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
import javax.inject.Inject;
import javax.inject.Singleton;


/**
 * Default implementation for LinkRepository.
 */
@Singleton
public class LinkRepositoryImpl implements LinkRepository {

  @Inject
  private BigTableManager bigTableManager;

  /**
   * Returns short link object for alias.
   *
   * @param shortLink alias
   * @return short link if it has been found
   */
  @Override
  public Optional<ShortLink> findByShortLink(String shortLink) {
    try {
      return bigTableManager.findShortLink(shortLink);
    } catch (IOException exception) {
      throw new BigTableException(exception);
    }
  }

  /**
   * Removes link from BigTable.
   *
   * @param email of user
   * @param shortLink to delete
   * @return true if successful
   */
  @Override
  public boolean deleteLink(String email, String shortLink) {
    try {
      Optional<ShortLink> link = bigTableManager.findShortLink(shortLink);
      if (link.isEmpty()) {
        return false;
      }
      if (link.get().userEmail().equals(email)) {
        bigTableManager.deleteLink(shortLink);
        return true;
      } else {
        return false;
      }
    } catch (IOException exception) {
      throw new BigTableException(exception);
    }
  }

  /**
   * Returns list of aliases of specified user.
   *
   * @param email od user
   * @return ArrayList
   */
  @Override
  public ArrayList<ShortLink> getLinksOfUser(String email) {
    try {
      return bigTableManager.listAllUserLinks(email);
    } catch (IOException exception) {
      throw new BigTableException(exception);
    }
  }

  /**
   * Persists provided link.
   *
   * @param link to save
   */
  @Override
  public void saveLink(ShortLink link) {
    try {
      bigTableManager.storeLink(link);
    } catch (IOException exception) {
      throw new BigTableException(exception);
    }
  }
}
