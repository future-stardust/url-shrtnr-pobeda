package edu.kpi.testcourse;

import edu.kpi.testcourse.bigtable.BigTable;
import edu.kpi.testcourse.bigtable.BigTableConf;
import edu.kpi.testcourse.bigtable.BigTableImpl;
import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Primary;
import io.micronaut.context.annotation.Requires;
import java.io.IOException;
import java.nio.file.Files;
import javax.inject.Singleton;

// If some beans will be in need of extensive configuration in future
// then it should be done here

/**
 * Factory for something.
 */
@Factory
public class BeanFactory {

  private static final String PATH_TO_STORAGE_WINDOWS =
      System.getenv("appdata") + "\\url-shrtnr-pobeda";
  private static final String PATH_TO_STORAGE_LINUX =
      System.getProperty("user.home") + "/.url-shrtnr-pobeda";


  @Requires(notEnv = "test")
  @Primary
  @Singleton
  BigTable createBigTable() {
    // Check OS and select correct configuration
    String osName = System.getProperty("os.name").toLowerCase();
    BigTableConf conf;
    if ((osName.contains("win"))) {
      conf = new BigTableConf(PATH_TO_STORAGE_WINDOWS);
    } else {
      conf = new BigTableConf(PATH_TO_STORAGE_LINUX);
    }

    return new BigTableImpl(conf);
  }

  @Singleton
  BigTable createTestBigTable() {
    try {
      return new BigTableImpl(
          new BigTableConf(Files.createTempDirectory("url-shrtnr-pobeda-tests")));
    } catch (IOException exception) {
      throw new RuntimeException(exception);
    }
  }
}
