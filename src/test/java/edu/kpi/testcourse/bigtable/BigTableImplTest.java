package edu.kpi.testcourse.bigtable;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class BigTableImplTest {

  @Test
  void checkValueSaving() {
    BigTableImpl bigTable = new BigTableImpl();

    String testData = "test_value";
    bigTable.store("test", testData);
    String resp = bigTable.read("test");

    assertThat(testData).isEqualTo(resp);
  }

}
