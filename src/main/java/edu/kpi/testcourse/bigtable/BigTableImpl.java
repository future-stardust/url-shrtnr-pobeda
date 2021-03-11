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

  public BigTableImpl() {
    if (!Files.exists(Paths.get(DATA_FOLDER_NAME))) {
      try {
        Files.createDirectory(Paths.get(DATA_FOLDER_NAME));
      } catch (IOException ex) {
        ex.printStackTrace();
      }
    }
  }

  @Override
  public void store(String name, String value) throws IOException {
    if (Files.exists(Paths.get(DATA_FOLDER_NAME, name))) {
      throw new FileAlreadyExistsException(String.format("%s already exists",
        Paths.get(DATA_FOLDER_NAME, name).toString()));
    }
    Files.write(Paths.get(DATA_FOLDER_NAME, name), value.getBytes(), StandardOpenOption.CREATE);
  }

  @Override
  public String read(String name) throws IOException {
    if (!Files.exists(Paths.get(DATA_FOLDER_NAME, name))) {
      throw new FileNotFoundException(String.format("%s does not exist",
        Paths.get(DATA_FOLDER_NAME, name).toString()));
    }
    return Files.readString(Paths.get(DATA_FOLDER_NAME, name));
  }

  @Override
  public void delete(String name) throws IOException {
    if (Files.exists(Paths.get(DATA_FOLDER_NAME, name))) {
      Files.delete(Paths.get(DATA_FOLDER_NAME, name));
    } else {
      throw new FileNotFoundException(String.format("%s does not exist",
        Paths.get(DATA_FOLDER_NAME, name).toString()));
    }
  }

  @Override
  public Path getDir() {
    return Paths.get(DATA_FOLDER_NAME);
  }
}
