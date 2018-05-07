package com.hubspot.rosetta.jdbi3;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Test;

public class BindWithRosettaTest extends AbstractJdbiTest {

  @Test
  public void itBindsObject() {
    TestObject expected = new TestObject(1, "test");

    getDao().insert(expected);

    TestObject actual = getDao().withHandle(handle -> {
      List<TestObject> results = handle
          .createQuery("SELECT * FROM test_table")
          .map((rs, ctx) -> {
            int id = rs.getInt("id");
            String name = rs.getString("name");

            return new TestObject(id, name);
          })
          .list();

      assertThat(results).hasSize(1);
      return results.get(0);
    });

    assertThat(actual).isEqualTo(expected);
  }
}
