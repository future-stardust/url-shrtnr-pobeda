package edu.kpi.testcourse.bigtable;


import edu.kpi.testcourse.Main;
import edu.kpi.testcourse.dto.User;
import edu.kpi.testcourse.dto.ShortLink;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Optional;
import javax.inject.Inject;
import org.assertj.core.api.Fail;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
public class BigTableManagerTest {

  @Inject
  private BigTable bigTable;
  @Inject
  private BigTableManagerImpl bigTableManager;

  private final User testUser = new User("testMail", "testPass");
  private final ShortLink shortLink = new ShortLink("testShort", "testMail", "https://google.com");
  private final String testUserJson = Main.getGson().toJson(testUser, User.class);
  private final String shortLinkJson = Main.getGson().toJson(shortLink, ShortLink.class);

  @AfterEach
  public void cleanup() {
    try {
      if (Files.exists(bigTable.getDir(DataFolder.Users).resolve(testUser.email()))) {
        bigTable.delete(testUser.email(), DataFolder.Users);
      }
      if (Files.exists(bigTable.getDir(DataFolder.Links).resolve(shortLink.alias()))) {
        bigTable.delete(shortLink.alias(), DataFolder.Links);
      }
    } catch (IOException exception) {
      throw new RuntimeException(exception);
    }
  }

  @Test
  public void managerStoresUserTest() {
    assertDoesNotThrow(() -> bigTableManager.storeUser(testUser));
    assertDoesNotThrow(() -> assertEquals(bigTable.read(testUser.email(), DataFolder.Users),
      testUserJson));
  }

  @Test
  public void exceptionWhenUserAlreadyExists() {
    assertDoesNotThrow(() -> bigTableManager.storeUser(testUser));

    assertThrows(IOException.class, () -> bigTableManager.storeUser(testUser));
  }

  @Test
  public void managerFindsExistingUser() {
    assertDoesNotThrow(() -> bigTableManager.storeUser(testUser));

    assertDoesNotThrow(() -> {
      Optional<User> resp = bigTableManager.findEmail(testUser.email());
      assertTrue(resp.isPresent());
      assertEquals(testUser.email(), resp.get().email());
      assertEquals(testUser.password(), resp.get().password());
    });
  }

  @Test
  public void exceptionWhenManagerDoesNotFindUser() {
    assertDoesNotThrow(() -> assertTrue(bigTableManager.findEmail("notActualMail").isEmpty()));
  }

  @Test
  public void managerStoresLink() {
    assertDoesNotThrow(() -> bigTableManager.storeLink(shortLink));
    assertDoesNotThrow(
      () -> assertEquals(shortLinkJson, bigTable.read(shortLink.alias(), DataFolder.Links)));
  }

  @Test
  public void managerFindsAllLinksOfUser() {
    assertDoesNotThrow(() -> bigTableManager.storeLink(shortLink));
    assertDoesNotThrow(() -> {
      ArrayList<ShortLink> list = bigTableManager.listAllUserLinks(shortLink.email());
      assertEquals(1, list.size());
      ShortLink resp = list.get(0);
      assertEquals(shortLink.email(), resp.email());
      assertEquals(shortLink.alias(), resp.alias());
      assertEquals(shortLink.url(), resp.url());
    });
  }

  @Test
  public void managerDeletesLink() {
    assertDoesNotThrow(() -> bigTableManager.storeLink(shortLink));
    assertDoesNotThrow(() -> bigTableManager.deleteLink(shortLink.alias()));
  }

  @Test
  public void managerFindsShortLinkByAlias() {
    assertDoesNotThrow(() -> bigTableManager.storeLink(shortLink));
    assertDoesNotThrow(() -> {
      Optional<ShortLink> respOpt = bigTableManager.findShortLink(shortLink.alias());
      if (respOpt.isEmpty()) {
        fail();
      }
      ShortLink resp = respOpt.get();
      assertEquals(shortLink.email(), resp.email());
      assertEquals(shortLink.alias(), resp.alias());
      assertEquals(shortLink.url(), resp.url());
    });
  }

  @Test
  public void returnsEmptyWhenLinkDoesNotExist() {
    assertDoesNotThrow(
      () -> assertEquals(Optional.empty(), bigTableManager.findShortLink(shortLink.alias())));
  }
}
