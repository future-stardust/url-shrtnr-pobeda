package edu.kpi.testcourse.repository;

import edu.kpi.testcourse.logic.ShortLinkMock;
import java.util.ArrayList;
import java.util.Optional;

/**
 * Interface for link repo.
 */
public interface LinkRepository {

  Optional<ShortLinkMock> findByShortLink(String shortLink);

  boolean deleteLink(String email, String shortLink);

  ArrayList<ShortLinkMock> getLinksOfUser(String email);

  void saveLink(ShortLinkMock link);
}
