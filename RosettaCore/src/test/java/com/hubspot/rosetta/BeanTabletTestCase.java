package com.hubspot.rosetta;

import static org.fest.assertions.api.Assertions.assertThat;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.hubspot.rosetta.beans.MaybePerson;
import com.hubspot.rosetta.beans.Parent;

public class BeanTabletTestCase {

  private Tablet tablet;

  @Before
  public void setUp() {
    tablet = Rosetta.tabletForType(Parent.class);
  }

  @Test
  public void bindableFieldsForSimpleBean() {
    Set<String> bindable = tablet.getFields(MaybePerson.class, false).keySet();
    assertThat(bindable).hasSize(3);
    assertThat(bindable).contains("name");
    assertThat(bindable).contains("age");
    assertThat(bindable).contains("living");
  }

  @Test
  public void bindableFieldsForNestedBean() {
    Set<String> bindable = tablet.getFields(Parent.class, false).keySet();
    assertThat(bindable).hasSize(4);
    assertThat(bindable).contains("child.name");
    assertThat(bindable).contains("child.age");
  }

}
