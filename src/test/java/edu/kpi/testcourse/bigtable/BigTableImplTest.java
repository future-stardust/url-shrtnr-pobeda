package edu.kpi.testcourse.bigtable;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import java.io.IOException;
import java.nio.file.Files;
import javax.inject.Inject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

@MicronautTest
class BigTableImplTest {

  @Inject
  private BigTable bigTable;

  private final String testName = "testName";

  @AfterEach
  public void cleanup() {
    try {
      Files.walk(bigTable.getDir(null)).forEach((p) -> {
        if (!Files.isDirectory(p)) {
          try {
            Files.delete(p);
          } catch (IOException exception) {
            throw new RuntimeException(exception);
          }
        }
      });
    } catch (IOException exception) {
      throw new RuntimeException(exception);
    }
  }

  @Test
  void checkIfBigTableCreatedCorrectly() {
    assertTrue(Files.exists(bigTable.getDir(null)));
    assertTrue(Files.exists(bigTable.getDir(DataFolder.Links)));
    assertTrue(Files.exists(bigTable.getDir(DataFolder.Users)));
    assertTrue(Files.exists(bigTable.getDir(DataFolder.Sessions)));
  }

  @Test
  void fileCreationDeletionTest() {
    assertDoesNotThrow(() -> bigTable.store(testName + 1, "testValue", null));
    assertDoesNotThrow(() -> bigTable.delete(testName + 1, null));
  }

  @Test
  void valueSavingTest() {
    String testValue = "testValue";
    assertDoesNotThrow(() -> bigTable.store(testName, testValue, null));

    assertDoesNotThrow(() -> {
      String resp = bigTable.read(testName, null);
      assertThat(resp).isEqualTo("testValue");
    });

    assertDoesNotThrow(() -> bigTable.delete(testName, null));
  }

  @Test
  void exceptionStoreFileAlreadyExistsTest() {
    assertDoesNotThrow(() -> bigTable.store(testName, "testValue", null));
    assertThrows(IOException.class, () -> bigTable.store(testName, "newTestValue", null));
    assertDoesNotThrow(() -> bigTable.delete(testName, null));
  }

  @Test
  void exceptionReadFileDoesNotExist() {
    assertThrows(IOException.class, () -> bigTable.read("newTestName", null));
  }

  @Test
  void exceptionDeleteFileDoesNotExist() {
    assertThrows(IOException.class, () -> bigTable.delete("newTestName", null));
  }
}
