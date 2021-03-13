package edu.kpi.testcourse.bigtable;

import edu.kpi.testcourse.dto.ShortLink;
import edu.kpi.testcourse.dto.User;
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

  Optional<ShortLink> findShortLink(String shortLink) throws IOException;

  void deleteLink(String shortLink) throws IOException;

  ArrayList<ShortLink> listAllUserLinks(String email) throws IOException;

  void storeLink(ShortLink link) throws IOException;
}
