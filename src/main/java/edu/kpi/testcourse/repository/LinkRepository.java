package edu.kpi.testcourse.repository;

import edu.kpi.testcourse.logic.ShortLinkMock;
import java.util.ArrayList;
import java.util.Optional;

public interface LinkRepository {

  Optional<ShortLinkMock> findByShortLink(String shortLink);

  boolean deleteLink(String email, String shortLink);

  ArrayList<ShortLinkMock> getLinksOfUser(String email);

  void saveLink(ShortLinkMock link);
}
