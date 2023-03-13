package com.hubspot.rosetta.jdbi3;

import static org.assertj.core.api.Assertions.assertThat;
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
}
