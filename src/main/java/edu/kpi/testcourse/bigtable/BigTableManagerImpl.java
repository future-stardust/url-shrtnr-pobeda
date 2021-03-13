package edu.kpi.testcourse.bigtable;

import edu.kpi.testcourse.Main;
import edu.kpi.testcourse.dto.User;
import edu.kpi.testcourse.dto.ShortLink;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Implementation of BigTableManager.
 */
@Singleton
public class BigTableManagerImpl implements BigTableManager {

  @Inject
  private BigTable bigTable;

  /**
   * Finds user in stored data.
   *
   * @param email to search
   * @return Optional of User
   * @throws IOException when email hasn't found
   */
  @Override
  public Optional<User> findEmail(String email) throws IOException {
    Path dataFolder = bigTable.getDir(DataFolder.Users);
    if (Files.exists(Path.of(dataFolder.toString(), email))) {
      return Optional.of(Main.getGson().fromJson(bigTable.read(email, DataFolder.Users),
        User.class));
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
    bigTable.store(user.email(), json, DataFolder.Users);
  }

  @Override
  public Optional<ShortLink> findShortLink(String shortLink) throws IOException {
    Path dataFolder = bigTable.getDir(DataFolder.Links);
    if (Files.exists(Path.of(dataFolder.toString(), shortLink))) {
      return Optional.of(Main.getGson().fromJson(bigTable.read(shortLink, DataFolder.Links),
        ShortLink.class));
    } else {
      return Optional.empty();
    }
  }

  @Override
  public void deleteLink(String shortLink) throws IOException {
    bigTable.delete(shortLink, DataFolder.Links);
  }

  @Override
  public ArrayList<ShortLink> listAllUserLinks(String email) throws IOException {
    Path path = bigTable.getDir(DataFolder.Links);
    return Files.walk(path)
      .map((p) -> {
        try {
          return Main.getGson().fromJson(Files.readString(p), ShortLink.class);
        } catch (IOException exception) {
          exception.printStackTrace();
        }
        return null;
      })
      .filter(Objects::nonNull)
      .filter((s) -> s.userEmail().equals(email))
      .collect(Collectors.toCollection(ArrayList::new));
  }

  @Override
  public void storeLink(ShortLink link) throws IOException {
    String json = Main.getGson().toJson(link);
    bigTable.store(link.shortLink(), json, DataFolder.Links);
  }
}
