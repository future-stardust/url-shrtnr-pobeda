package edu.kpi.testcourse.bigtable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

class BigTableImplTest {

  private static BigTableImpl bigTable;
  private static String testName;

  @BeforeAll
  public static void init() {
    bigTable = new BigTableImpl();
    testName = "testName";
  }

  @Test
  void checkIfBigTableCreatedCorrectly()
  {
    assertTrue(Files.exists(Paths.get("data")));
  }

  @Test
  void fileCreationDeletionTest() {
    assertDoesNotThrow(() -> bigTable.store(testName + 1, "testValue"));
    assertDoesNotThrow(() -> bigTable.delete(testName + 1));
  }

  @Test
  void valueSavingTest() {
    String testValue = "testValue";
    assertDoesNotThrow(() -> bigTable.store(testName, testValue));

    assertDoesNotThrow(() -> {
      String resp = bigTable.read(testName);
      assertThat(resp).isEqualTo("testValue");
    });

    assertDoesNotThrow(() -> bigTable.delete(testName));
  }

  @Test
  void exceptionStoreFileAlreadyExistsTest()
  {
    assertDoesNotThrow(() -> bigTable.store(testName, "testValue"));
    assertThrows(IOException.class, () -> bigTable.store(testName, "newTestValue"));
    assertDoesNotThrow(() -> bigTable.delete(testName));
  }

  @Test
  void exceptionReadFileDoesNotExist()
  {
    assertThrows(IOException.class, () -> bigTable.read("newTestName"));
  }

  @Test
  void exceptionDeleteFileDoesNotExist()
  {
    assertThrows(IOException.class, () -> bigTable.delete("newTestName"));
  }
}
