package com.hubspot.rosetta.jdbi3;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.Test;

public class NestedOptionalTest extends AbstractJdbiTest {

  @Test
  public void itDeserializesNestedObject() {
    TestObject firstObject = new TestObject(1, "name-1");
    TestRelatedObject relatedObject = new TestRelatedObject(1, 1, "related-name-1", 12);

    TestViewObject firstExpectedView = new TestViewObject(1, "name-1", Optional.of(relatedObject));

    TestObject secondObject = new TestObject(2, "name-2");

    TestViewObject secondExpectedView = new TestViewObject(2, "name-2", Optional.empty());

    getDao().insert(firstObject);
    getDao().insert(relatedObject);

    getDao().insert(secondObject);

    assertThat(getDao().getAllView()).contains(firstExpectedView, secondExpectedView);
  }

  @Test
  public void itDeserializedSubTypedNestedObject() {

  }
}
