package com.hubspot.rosetta.jdbi;

import com.hubspot.rosetta.beans.NestedBean;
import com.hubspot.rosetta.beans.RosettaCreatorConstructorBean;
import com.hubspot.rosetta.beans.RosettaCreatorMethodBean;
import com.hubspot.rosetta.beans.RosettaNamingBean;
import com.hubspot.rosetta.beans.RosettaValueBean;
import com.hubspot.rosetta.beans.StoredAsJsonBean;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.fest.assertions.api.Assertions.assertThat;

public class RosettaMapperFactoryTest {

  private final RosettaMapperFactory mapperFactory = new RosettaMapperFactory();

  @Test
  public void itRejectsInteger() {
    assertThat(mapperFactory.accepts(Integer.class, null)).isFalse();
  }

  @Test
  public void itRejectsInt() {
    assertThat(mapperFactory.accepts(Integer.TYPE, null)).isFalse();
  }

  @Test
  public void itAcceptsRosettaCreatorConstructorBean() {
    assertThat(mapperFactory.accepts(RosettaCreatorConstructorBean.class, null)).isTrue();
  }

  @Test
  public void itAcceptsRosettaCreatorMethodBean() {
    assertThat(mapperFactory.accepts(RosettaCreatorMethodBean.class, null)).isTrue();
 }

  @Test
  public void itAcceptsRosettaNamingBean() {
    assertThat(mapperFactory.accepts(RosettaNamingBean.class, null)).isTrue();
  }

  @Test
  public void itAcceptsRosettaValueBean() {
    assertThat(mapperFactory.accepts(RosettaValueBean.class, null)).isTrue();
  }

  @Test
  public void itAcceptsStoredAsJsonBean() {
    assertThat(mapperFactory.accepts(StoredAsJsonBean.class, null)).isTrue();
  }

  @Test
  public void itAcceptsNestedBean() {
    assertThat(mapperFactory.accepts(NestedBean.class, null)).isTrue();
  }

  @Test
  public void itAcceptsCircularBean() {
    assertThat(mapperFactory.accepts(CircularBean.class, null)).isTrue();
  }
}
