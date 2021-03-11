package edu.kpi.testcourse.bigtable;


import edu.kpi.testcourse.Main;
import edu.kpi.testcourse.dto.User;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
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
  private static String testUserJson;

  @BeforeAll
  public static void init() {
    testUser = new User("testMail", "testPass");
    testUserJson = Main.getGson().toJson(testUser, User.class);
  }

  @BeforeEach
  public void cleanup() {
    if (Files.exists(Paths.get(bigTable.getDir().toString(), testUser.email()))) {
      try {
        bigTable.delete(testUser.email());
      } catch (IOException exception) {
        exception.printStackTrace();
      }
    }
  }

  @Test
  public void ManagerStoresUserTest() {
    assertDoesNotThrow(() -> bigTableManager.storeUser(testUser));
    assertDoesNotThrow(() -> assertEquals(bigTable.read(testUser.email()), testUserJson));
  }

  @Test
  public void ExceptionWhenUserAlreadyExists() {
    assertDoesNotThrow(() -> bigTableManager.storeUser(testUser));

    assertThrows(IOException.class, () -> bigTableManager.storeUser(testUser));
  }

  @Test
  public void ManagerFindsExistingUser() {
    assertDoesNotThrow(() -> bigTableManager.storeUser(testUser));

    assertDoesNotThrow(() -> {
      Optional<User> resp = bigTableManager.findEmail(testUser.email());
      assertTrue(resp.isPresent());
      assertEquals(testUser.email(), resp.get().email());
      assertEquals(testUser.password(), resp.get().password());
    });
  }

  @Test
  public void ExceptionWhenManagerDoesNotFindUser() {
    assertDoesNotThrow(() -> assertTrue(bigTableManager.findEmail("notActualMail").isEmpty()));
  }
}
