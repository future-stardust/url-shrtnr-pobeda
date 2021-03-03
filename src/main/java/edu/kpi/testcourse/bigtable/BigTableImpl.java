package edu.kpi.testcourse.bigtable;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

// ️ Please, pay attention, that you should not use any 3rd party persistence library (e.g. data
// ️ base, implementation of key-value storage, etc)

class BigTableImpl implements BigTable {

  @Override
  public void store(String name, String value) {

  }

  @Override
  public String read(String name) {
    return null;
  }

  @Override
  public void delete(String name) {

  }
}
