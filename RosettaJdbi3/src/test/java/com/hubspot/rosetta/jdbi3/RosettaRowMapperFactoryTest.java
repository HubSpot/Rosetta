package com.hubspot.rosetta.jdbi3;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;
import org.junit.Test;

public class RosettaRowMapperFactoryTest extends AbstractJdbiTest {

  @Test
  public void itMapsObject() {
    TestObject expected = new TestObject(1, "test");

    getDao()
      .useHandle(handle -> {
        handle.execute("INSERT INTO test_table (id, name) VALUES (1, 'test')");
      });

    List<TestObject> results = getDao().getAll();
    assertThat(results).hasSize(1);

    TestObject actual = results.get(0);
    assertThat(actual).isEqualTo(expected);

    Map<Integer, TestObject> map = getDao().getAllMap();
    assertThat(map).containsOnly(Map.entry(1, expected));
  }
}
