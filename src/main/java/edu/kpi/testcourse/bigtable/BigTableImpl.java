package edu.kpi.testcourse.bigtable;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import javax.annotation.Nullable;
import javax.inject.Singleton;

/**
 * Basic class for basic operations with file system.
 */
@Singleton
public class BigTableImpl implements BigTable {

  private final BigTableConf conf;

  /**
   * Constructor, now uses conf file.
   *
   * @param conf configuration
   */
  public BigTableImpl(BigTableConf conf) {
    this.conf = conf;

    try {
      if (!Files.exists(conf.storage())) {
        Files.createDirectory(conf.storage());
      }
      if (!Files.exists(conf.links())) {
        Files.createDirectory(conf.links());
      }
      if (!Files.exists(conf.users())) {
        Files.createDirectory(conf.users());
      }
      if (!Files.exists(conf.sessions())) {
        Files.createDirectory(conf.sessions());
      }
    } catch (IOException ex) {
      throw new RuntimeException(ex);
    }
  }

  @Override
  public void store(String name, String value, @Nullable DataFolder folder) throws IOException {
    Path path;
    if (folder == DataFolder.Links) {
      path = conf.links().resolve(name);
    } else if (folder == DataFolder.Users) {
      path = conf.users().resolve(name);
    } else if (folder == DataFolder.Sessions) {
      path = conf.sessions().resolve(name);
    } else {
      path = conf.storage().resolve(name);
    }

    if (Files.exists(path)) {
      throw new FileAlreadyExistsException(
        String.format("%s already exists", path.getFileName().toString()));
    }
    Files.write(path, value.getBytes(), StandardOpenOption.CREATE);
  }

  @Override
  public String read(String name, @Nullable DataFolder folder) throws IOException {
    Path path;
    if (folder == DataFolder.Links) {
      path = conf.links().resolve(name);
    } else if (folder == DataFolder.Users) {
      path = conf.users().resolve(name);
    } else if (folder == DataFolder.Sessions) {
      path = conf.sessions().resolve(name);
    } else {
      path = conf.storage().resolve(name);
    }

    if (!Files.exists(path)) {
      throw new FileNotFoundException(
        String.format("%s does not exist", path.getFileName().toString()));
    }
    return Files.readString(path);
  }

  @Override
  public void delete(String name, @Nullable DataFolder folder) throws IOException {
    Path path;
    if (folder == DataFolder.Links) {
      path = conf.links().resolve(name);
    } else if (folder == DataFolder.Users) {
      path = conf.users().resolve(name);
    } else if (folder == DataFolder.Sessions) {
      path = conf.sessions().resolve(name);
    } else {
      path = conf.storage().resolve(name);
    }

    if (Files.exists(path)) {
      Files.delete(path);
    } else {
      throw new FileNotFoundException(
        String.format("%s does not exist", path.getFileName().toString()));
    }
  }

  @Override
  public Path getDir(@Nullable DataFolder folder) {
    if (folder == DataFolder.Links) {
      return conf.links();
    } else if (folder == DataFolder.Users) {
      return conf.users();
    } else if (folder == DataFolder.Sessions) {
      return conf.sessions();
    }
    return conf.storage();
  }
}
