package edu.kpi.testcourse;

import edu.kpi.testcourse.bigtable.BigTable;
import edu.kpi.testcourse.bigtable.BigTableConf;
import edu.kpi.testcourse.bigtable.BigTableImpl;
import io.micronaut.context.annotation.Factory;
import javax.inject.Singleton;

@Factory
public class BeanFactory {

  private final String PATH_TO_STORAGE_WINDOWS =
    System.getenv("appdata") + "\\url-shrtnr-pobeda";
  private final String PATH_TO_STORAGE_LINUX = "var/lib/url-shrtnr-pobeda";

  @Singleton
  BigTable createBigTable() {
    // Check OS and select correct configuration
    String osName = System.getProperty("os.name");
    BigTableConf conf;
    if ((osName.contains("win"))) {
      conf = new BigTableConf(PATH_TO_STORAGE_WINDOWS);
    } else {
      conf = new BigTableConf(PATH_TO_STORAGE_LINUX);
    }

    return new BigTableImpl(conf);
  }

}
