package edu.kpi.testcourse.repository;

import edu.kpi.testcourse.dto.ShortLink;
import java.util.ArrayList;
import java.util.Optional;

/**
 * Interface for link repo.
 */
public interface LinkRepository {

  Optional<ShortLink> findByShortLink(String shortLink);

  boolean deleteLink(String email, String shortLink);

  ArrayList<ShortLink> getLinksOfUser(String email);

  void saveLink(ShortLink link);
}
