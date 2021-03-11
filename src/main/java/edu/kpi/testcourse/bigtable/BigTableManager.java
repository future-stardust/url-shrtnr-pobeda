package edu.kpi.testcourse.bigtable;

import edu.kpi.testcourse.dto.User;
import edu.kpi.testcourse.logic.ShortLinkMock;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

/**
 * Interface for facade of BigTable.
 * Provides more high level operations using BigTable.
 */
public interface BigTableManager {

  Optional<User> findEmail(String email) throws IOException;

  void storeUser(User user) throws IOException;

  Optional<ShortLinkMock> findShortLink(String shortLink) throws IOException;

  void deleteLink(String shortLink) throws IOException;

  ArrayList<ShortLinkMock> listAllUserLinks(String email) throws IOException;

  void storeLink(ShortLinkMock link) throws IOException;
}
