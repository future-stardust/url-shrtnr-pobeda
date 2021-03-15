package edu.kpi.testcourse.bigtable;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.inject.Inject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

@MicronautTest
class BigTableImplTest {

  @Inject
  private static BigTable bigTable;
  private static String testName;

  @BeforeAll
  public static void init() {
    testName = "testName";
  }

  @Test
  void checkIfBigTableCreatedCorrectly()
  {
    assertTrue(Files.exists(Paths.get("data")));
  }

  @Test
  void fileCreationDeletionTest() {
    System.out.println(System.getenv("appdata"));
    assertDoesNotThrow(() -> bigTable.store(testName + 1, "testValue",
      DataFolder.Links));
    assertDoesNotThrow(() -> bigTable.delete(testName + 1, DataFolder.Links));
  }

  @Test
  void valueSavingTest() {
    String testValue = "testValue";
    assertDoesNotThrow(() -> bigTable.store(testName, testValue, DataFolder.Links));

    assertDoesNotThrow(() -> {
      String resp = bigTable.read(testName, DataFolder.Links);
      assertThat(resp).isEqualTo("testValue");
    });

    assertDoesNotThrow(() -> bigTable.delete(testName, DataFolder.Links));
  }

  @Test
  void exceptionStoreFileAlreadyExistsTest()
  {
    assertDoesNotThrow(() -> bigTable.store(testName, "testValue", DataFolder.Links));
    assertThrows(IOException.class, () -> bigTable.store(testName, "newTestValue",
      DataFolder.Links));
    assertDoesNotThrow(() -> bigTable.delete(testName, DataFolder.Links));
  }

  @Test
  void exceptionReadFileDoesNotExist()
  {
    assertThrows(IOException.class, () -> bigTable.read("newTestName", DataFolder.Links));
  }

  @Test
  void exceptionDeleteFileDoesNotExist()
  {
    assertThrows(IOException.class, () -> bigTable.delete("newTestName", DataFolder.Links));
  }
}
