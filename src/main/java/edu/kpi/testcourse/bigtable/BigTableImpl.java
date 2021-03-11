package edu.kpi.testcourse.bigtable;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import javax.inject.Singleton;

// Please, pay attention, that you should not use any 3rd party persistence library (e.g. data
// base, implementation of key-value storage, etc)

@Singleton
class BigTableImpl implements BigTable {

  private static final String DATA_FOLDER_NAME = "data";
  private static final String USERS_FOLDER_NAME = "users";
  private static final String LINKS_FOLDER_NAME = "links";

  public BigTableImpl() {
    if (!Files.exists(Paths.get(DATA_FOLDER_NAME))) {
      try {
        Files.createDirectory(Paths.get(DATA_FOLDER_NAME));
      } catch (IOException ex) {
        ex.printStackTrace();
      }
    }
    if (!Files.exists(Paths.get(DATA_FOLDER_NAME, USERS_FOLDER_NAME))) {
      try {
        Files.createDirectory(Paths.get(DATA_FOLDER_NAME, USERS_FOLDER_NAME));
      } catch (IOException ex) {
        ex.printStackTrace();
      }
    }
    if (!Files.exists(Paths.get(DATA_FOLDER_NAME, LINKS_FOLDER_NAME))) {
      try {
        Files.createDirectory(Paths.get(DATA_FOLDER_NAME, LINKS_FOLDER_NAME));
      } catch (IOException ex) {
        ex.printStackTrace();
      }
    }
  }

  @Override
  public void store(String name, String value, DataFolder folder) throws IOException {
    Path path = Paths.get(DATA_FOLDER_NAME, name);
    if (folder == DataFolder.Links) {
      path = Paths.get(DATA_FOLDER_NAME, LINKS_FOLDER_NAME, name);
    } else if (folder == DataFolder.Users) {
      path = Paths.get(DATA_FOLDER_NAME, USERS_FOLDER_NAME, name);
    }
    if (Files.exists(path)) {
      throw new FileAlreadyExistsException(String.format("%s already exists", path.toString()));
    }
    Files.write(path, value.getBytes(), StandardOpenOption.CREATE);
  }

  @Override
  public String read(String name, DataFolder folder) throws IOException {
    Path path = Paths.get(DATA_FOLDER_NAME, name);
    if (folder == DataFolder.Links) {
      path = Paths.get(DATA_FOLDER_NAME, LINKS_FOLDER_NAME, name);
    } else if (folder == DataFolder.Users) {
      path = Paths.get(DATA_FOLDER_NAME, USERS_FOLDER_NAME, name);
    }
    if (!Files.exists(path)) {
      throw new FileNotFoundException(String.format("%s does not exist", path.toString()));
    }
    return Files.readString(path);
  }

  @Override
  public void delete(String name, DataFolder folder) throws IOException {
    Path path = Paths.get(DATA_FOLDER_NAME, name);
    if (folder == DataFolder.Links) {
      path = Paths.get(DATA_FOLDER_NAME, LINKS_FOLDER_NAME, name);
    } else if (folder == DataFolder.Users) {
      path = Paths.get(DATA_FOLDER_NAME, USERS_FOLDER_NAME, name);
    }
    if (Files.exists(path)) {
      Files.delete(path);
    } else {
      throw new FileNotFoundException(String.format("%s does not exist", path.toString()));
    }
  }

  @Override
  public Path getDir(DataFolder folder) {
    if (folder == DataFolder.Links) {
      return Paths.get(DATA_FOLDER_NAME, LINKS_FOLDER_NAME);
    } else if (folder == DataFolder.Users) {
      return Paths.get(DATA_FOLDER_NAME, USERS_FOLDER_NAME);
    }
    return Paths.get(DATA_FOLDER_NAME);
  }
}
