package edu.kpi.testcourse.bigtable;


import edu.kpi.testcourse.Main;
import edu.kpi.testcourse.dto.User;
import edu.kpi.testcourse.dto.ShortLink;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Optional;
import javax.inject.Inject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
public class BigTableManagerTest {

  @Inject
  private BigTableImpl bigTable;
  @Inject
  private BigTableManagerImpl bigTableManager;

  private static User testUser;
  private static ShortLink shortLink;
  private static String testUserJson;
  private static String shortLinkJson;

  @BeforeAll
  public static void init() throws MalformedURLException {
    testUser = new User("testMail", "testPass");
    shortLink = new ShortLink("testShort", "testMail",
      new URL("https://google.com"));
    testUserJson = Main.getGson().toJson(testUser, User.class);
    shortLinkJson = Main.getGson().toJson(shortLink, ShortLink.class);
  }

  @BeforeEach
  public void cleanup() {
    if (Files.exists(Paths.get(bigTable.getDir(DataFolder.Users).toString(), testUser.email()))) {
      try {
        bigTable.delete(testUser.email(), DataFolder.Users);
      } catch (IOException exception) {
        exception.printStackTrace();
      }
    }
    if (Files.exists(Paths.get(bigTable.getDir(DataFolder.Links).toString(),
      shortLink.shortLink()))) {
      try {
        bigTable.delete(shortLink.shortLink(), DataFolder.Links);
      } catch (IOException exception) {
        exception.printStackTrace();
      }
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
    assertDoesNotThrow(() -> {
      assertEquals(shortLinkJson, bigTable.read(shortLink.shortLink(), DataFolder.Links));
    });
  }

  @Test
  public void managerFindsAllLinksOfUser() {
    assertDoesNotThrow(() -> bigTableManager.storeLink(shortLink));
    assertDoesNotThrow(() -> {
      ArrayList<ShortLink> list = bigTableManager.listAllUserLinks(shortLink.userEmail());
      assertEquals(1, list.size());
      ShortLink resp = list.get(0);
      assertEquals(shortLink.userEmail(), resp.userEmail());
      assertEquals(shortLink.shortLink(), resp.shortLink());
      assertEquals(shortLink.destination(), resp.destination());
    });
  }

  @Test
  public void managerDeletesLink() {
    assertDoesNotThrow(() -> bigTableManager.storeLink(shortLink));
    assertDoesNotThrow(() -> bigTableManager.deleteLink(shortLink.shortLink()));
  }

  @Test
  public void managerFindsShortLinkByAlias() {
    assertDoesNotThrow(() -> bigTableManager.storeLink(shortLink));
    assertDoesNotThrow(() -> {
      ShortLink resp = bigTableManager.findShortLink(shortLink.shortLink()).get();
      assertEquals(shortLink.userEmail(), resp.userEmail());
      assertEquals(shortLink.shortLink(), resp.shortLink());
      assertEquals(shortLink.destination(), resp.destination());
    });
  }

  @Test
  public void returnsEmptyWhenLinkDoesNotExist() {
    assertDoesNotThrow(() -> {
      assertEquals(Optional.empty(), bigTableManager.findShortLink(shortLink.shortLink()));
    });
  }
}
