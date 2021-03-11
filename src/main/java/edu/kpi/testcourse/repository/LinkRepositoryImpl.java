package edu.kpi.testcourse.repository;

import edu.kpi.testcourse.bigtable.BigTableManager;
import edu.kpi.testcourse.exception.bigtable.BigTableException;
import edu.kpi.testcourse.logic.ShortLinkMock;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class LinkRepositoryImpl implements LinkRepository {

  @Inject
  private BigTableManager bigTableManager;

  @Override
  public Optional<ShortLinkMock> findByShortLink(String shortLink) {
    try {
      return bigTableManager.findShortLink(shortLink);
    } catch (IOException exception) {
      throw new BigTableException(exception);
    }
  }

  @Override
  public boolean deleteLink(String email, String shortLink) {
    try {
      Optional<ShortLinkMock> link = bigTableManager.findShortLink(shortLink);
      if (link.isEmpty()) return false;
      if (link.get().userEmail().equals(email)) {
        bigTableManager.deleteLink(shortLink);
        return true;
      }
      else {
        return false;
      }
    } catch (IOException exception) {
      throw new BigTableException(exception);
    }
  }

  @Override
  public ArrayList<ShortLinkMock> getLinksOfUser(String email) {
    try {
      return bigTableManager.listAllUserLinks(email);
    } catch (IOException exception) {
      throw new BigTableException(exception);
    }
  }

  @Override
  public void saveLink(ShortLinkMock link) {
    try {
      bigTableManager.storeLink(link);
    } catch (IOException exception) {
      throw new BigTableException(exception);
    }
  }
}
