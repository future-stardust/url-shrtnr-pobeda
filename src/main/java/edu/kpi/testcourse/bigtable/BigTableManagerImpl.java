package edu.kpi.testcourse.bigtable;

import edu.kpi.testcourse.Main;
import edu.kpi.testcourse.dto.User;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Implementation of BigTableManager.
 */
@Singleton
public class BigTableManagerImpl implements BigTableManager {

  @Inject
  private BigTableImpl bigTable;

  /**
   * Finds user in stored data.
   *
   * @param email to search
   * @return Optional of User
   * @throws IOException when email hasn't found
   */
  @Override
  public Optional<User> findEmail(String email) throws IOException {
    Path dataFolder = bigTable.getDir();
    if (Files.exists(Path.of(dataFolder.toString(), email))) {
      return Optional.of(Main.getGson().fromJson(bigTable.read(email), User.class));
    } else {
      return Optional.empty();
    }
  }

  /**
   * Persists provided user's data.
   *
   * @param user to save
   * @throws IOException when provided user already exists
   */
  @Override
  public void storeUser(User user) throws IOException {
    String json = Main.getGson().toJson(user, User.class);
    bigTable.store(user.email(), json);
  }
}
