package com.hubspot.rosetta.jdbi3;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import java.util.Arrays;
import java.util.Collections;
import org.junit.Test;

public class BindListWithRosettaTest extends AbstractJdbiTest {

  @Test
  public void itCanBindLists() {
    TestListObject one = new TestListObject(1, TestEnum.A);
    TestListObject two = new TestListObject(2, TestEnum.B);
    TestListObject three = new TestListObject(3, TestEnum.B);
    TestListObject four = new TestListObject(4, TestEnum.C);

    getDao().insert(one);
    getDao().insert(two);
    getDao().insert(three);
    getDao().insert(four);

    assertThat(getDao().getWithValue(Collections.singletonList(TestEnum.A))).containsExactly(one);
    assertThat(getDao().getWithValue(Collections.singletonList(TestEnum.B))).containsExactly(two, three);
    assertThat(getDao().getWithValue(Arrays.asList(TestEnum.A, TestEnum.C))).containsExactlyInAnyOrder(one, four);
    assertThat(getDao().getWithValue(Arrays.asList(TestEnum.A, TestEnum.B, TestEnum.C)))
        .containsExactlyInAnyOrder(one, two, three, four);

    assertThat(getDao().getWithFieldValue(Collections.singletonList(one))).containsExactly(one);
    assertThat(getDao().getWithFieldValue(Collections.singletonList(two))).containsExactly(two, three);
    assertThat(getDao().getWithFieldValue(Arrays.asList(one, four))).containsExactlyInAnyOrder(one, four);
    assertThat(getDao().getWithFieldValue(Arrays.asList(one, two, four)))
        .containsExactlyInAnyOrder(one, two, three, four);
  }

  @Test
  public void itThrowsOnEmpty() {
    assertThatThrownBy(() -> getDao().getWithValue(Collections.emptyList())).isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("argument is empty");
  }

  @Test
  public void itCanTurnEmptyToNull() {
    TestListObject one = new TestListObject(1, TestEnum.A);
    TestListObject two = new TestListObject(2, TestEnum.B);

    getDao().insert(one);
    getDao().insert(two);

    // SELECT * WHERE id NOT IN (NULL) becomes SELECT * WHERE NULL so always returns empty list
    assertThat(getDao().getWithoutValueEmptyToNull(Collections.emptyList())).isEmpty();
  }

  @Test
  public void itCanTurnEmptyToEmptyToVoid() {
    TestListObject one = new TestListObject(1, TestEnum.A);
    TestListObject two = new TestListObject(2, TestEnum.B);

    getDao().insert(one);
    getDao().insert(two);

    assertThat(getDao().getWithoutValueEmptyToVoid(Collections.emptyList())).containsExactlyInAnyOrder(one, two);
  }

  @Test
  public void itThrowsOnBindingListOfLists() {
    TestListObject one = new TestListObject(1, TestEnum.A, Collections.emptyList());
    TestListObject two = new TestListObject(2, TestEnum.A, Arrays.asList("a", "b"));

    assertThatThrownBy(() -> getDao().getWithListFieldValue(Collections.singletonList(one)))
        .isInstanceOf(IllegalArgumentException.class);

    assertThatThrownBy(() -> getDao().getWithListFieldValue(Collections.singletonList(two)))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  public void itThrowsOnBindingListOfObjects() {
    TestListObject one = new TestListObject(1, TestEnum.A, null, new TestObject(5, "asdf"));

    assertThatThrownBy(() -> getDao().getWithObjectFieldValue(Collections.singletonList(one)))
        .isInstanceOf(IllegalArgumentException.class);
  }
}
