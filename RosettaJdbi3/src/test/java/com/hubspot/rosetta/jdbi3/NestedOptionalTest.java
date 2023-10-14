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
    TestObject firstObject = new TestObject(1, "name-1");
    TestGreenNestedObject firstRelatedObject = new TestGreenNestedObject(1, 1000L, "relax-song-1");

    TestObject secondObject = new TestObject(2, "name-2");
    TestRedNestedObject secondRelatedObject = new TestRedNestedObject(2, 300L);

    TestObject thirdObject = new TestObject(3, "name-3");

    TestSubTypedViewObject firstExpectedView = new TestSubTypedViewObject(1, "name-1", Optional.of(firstRelatedObject));
    TestSubTypedViewObject secondExpectedView = new TestSubTypedViewObject(2, "name-2", Optional.of(secondRelatedObject));
    TestSubTypedViewObject thirdExpectedView = new TestSubTypedViewObject(3, "name-3", Optional.empty());

    getDao().insert(firstObject);
    getDao().insert(firstRelatedObject);

    getDao().insert(secondObject);
    getDao().insert(secondRelatedObject);

    getDao().insert(thirdObject);

    assertThat(getDao().getAllSubTypedNestedView()).contains(firstExpectedView, secondExpectedView, thirdExpectedView);
  }
}
