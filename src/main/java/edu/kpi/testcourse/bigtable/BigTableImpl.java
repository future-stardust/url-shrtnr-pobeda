package edu.kpi.testcourse.bigtable;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import javax.annotation.Nullable;
import javax.inject.Singleton;

// Please, pay attention, that you should not use any 3rd party persistence library (e.g. data
// base, implementation of key-value storage, etc)

@Singleton
public class BigTableImpl implements BigTable {

  private final BigTableConf conf;

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
    } catch (IOException ex) {
      throw new RuntimeException(ex);
    }
  }

  @Override
  public void store(String name, String value, DataFolder folder) throws IOException {
    Path path = conf.storage();
    if (folder == DataFolder.Links) {
      path = conf.links();
    } else if (folder == DataFolder.Users) {
      path = conf.users();
    }
    if (Files.exists(path)) {
      throw new FileAlreadyExistsException(
        String.format("%s already exists", path.getFileName().toString()));
    }
    Files.write(path, value.getBytes(), StandardOpenOption.CREATE);
  }

  @Override
  public String read(String name, DataFolder folder) throws IOException {
    Path path = conf.storage();
    if (folder == DataFolder.Links) {
      path = conf.links();
    } else if (folder == DataFolder.Users) {
      path = conf.users();
    }
    if (!Files.exists(path)) {
      throw new FileNotFoundException(
        String.format("%s does not exist", path.getFileName().toString()));
    }
    return Files.readString(path);
  }

  @Override
  public void delete(String name, DataFolder folder) throws IOException {
    Path path = conf.storage();
    if (folder == DataFolder.Links) {
      path = conf.links();
    } else if (folder == DataFolder.Users) {
      path = conf.users();
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
    }
    return conf.storage();
  }
}
