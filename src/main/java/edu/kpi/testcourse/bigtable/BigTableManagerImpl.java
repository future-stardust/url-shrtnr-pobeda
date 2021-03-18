package edu.kpi.testcourse.bigtable;

import edu.kpi.testcourse.dto.ShortLink;
import edu.kpi.testcourse.dto.User;
import edu.kpi.testcourse.dto.UserSession;
import edu.kpi.testcourse.helper.JsonTool;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
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
  @Inject
  private JsonTool jsonTool;

  @Override
  public Optional<User> findEmail(String email) throws IOException {
    Path dataFolder = bigTable.getDir(DataFolder.Users);
    if (Files.exists(Path.of(dataFolder.toString(), email))) {
      return Optional.of(jsonTool.fromJson(bigTable.read(email, DataFolder.Users),
        User.class));
    } else {
      return Optional.empty();
    }
  }

  @Override
  public Optional<UserSession> findUserSessionByToken(String token) throws IOException {
    Path path = bigTable.getDir(DataFolder.Sessions);
    return Files.walk(path)
      .map((p) -> {
        try {
          if (!Files.isDirectory(p)) {
            return jsonTool.fromJson(Files.readString(p), UserSession.class);
          }
        } catch (IOException exception) {
          exception.printStackTrace();
        }
        return null;
      })
      .filter(Objects::nonNull)
      .filter((s) -> s.token().equals(token))
      .collect(Collectors.toCollection(ArrayList::new))
      .stream().findFirst();
  }

  @Override
  public void storeUser(User user) throws IOException {
    String json = jsonTool.toJson(user);
    bigTable.store(user.email(), json, DataFolder.Users);
  }

  @Override
  public void storeUserSession(UserSession userSession) throws IOException {
    String json = jsonTool.toJson(userSession);

    bigTable.store(userSession.id().toString(), json, DataFolder.Sessions);
  }

  @Override
  public Optional<ShortLink> findShortLink(String shortLink) throws IOException {
    Path dataFolder = bigTable.getDir(DataFolder.Links);
    if (Files.exists(Path.of(dataFolder.toString(), shortLink))) {
      return Optional.of(jsonTool.fromJson(bigTable.read(shortLink, DataFolder.Links),
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
  public void deleteUserSession(UUID id) throws IOException {
    bigTable.delete(id.toString(), DataFolder.Sessions);
  }

  @Override
  public ArrayList<ShortLink> listAllUserLinks(String email) throws IOException {
    Path path = bigTable.getDir(DataFolder.Links);
    return Files.walk(path)
      .map((p) -> {
        try {
          if (!Files.isDirectory(p)) {
            return jsonTool.fromJson(Files.readString(p), ShortLink.class);
          }
        } catch (IOException exception) {
          exception.printStackTrace();
        }
        return null;
      })
      .filter(Objects::nonNull)
      .filter((s) -> s.email().equals(email))
      .collect(Collectors.toCollection(ArrayList::new));
  }

  @Override
  public ArrayList<UserSession> listAllUserSessions(String email) throws IOException {
    Path path = bigTable.getDir(DataFolder.Sessions);
    return Files.walk(path)
      .map((p) -> {
        try {
          if (!Files.isDirectory(p)) {
            return jsonTool.fromJson(Files.readString(p), UserSession.class);
          }
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
    String json = jsonTool.toJson(link);
    bigTable.store(link.alias(), json, DataFolder.Links);
  }
}
