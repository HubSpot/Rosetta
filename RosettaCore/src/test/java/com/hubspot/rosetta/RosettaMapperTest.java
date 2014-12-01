package com.hubspot.rosetta;

import com.hubspot.rosetta.beans.NestedBean;
import com.hubspot.rosetta.beans.RosettaCreatorConstructorBean;
import com.hubspot.rosetta.beans.RosettaCreatorMethodBean;
import com.hubspot.rosetta.beans.RosettaNamingBean;
import com.hubspot.rosetta.beans.RosettaValueBean;
import com.hubspot.rosetta.beans.StoredAsJsonBean;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.when;

public class RosettaMapperTest {
  private final ResultSet resultSet = Mockito.mock(ResultSet.class);
  private final ResultSetMetaData resultSetMetaData = Mockito.mock(ResultSetMetaData.class);

  private final List<String> labels = new ArrayList<String>();
  private final List<String> values = new ArrayList<String>();

  @Before
  public void setup() throws SQLException {
    when(resultSet.next()).thenThrow(new SQLException());
    when(resultSet.getMetaData()).thenReturn(resultSetMetaData);
    when(resultSet.getObject(anyInt())).thenAnswer(new Answer<Object>() {

      @Override
      public Object answer(InvocationOnMock invocation) throws Throwable {
        int index = (Integer) invocation.getArguments()[0];
        return values.get(index - 1);
      }
    });

    when(resultSetMetaData.getColumnType(anyInt())).thenReturn(Types.VARCHAR);
    when(resultSetMetaData.getColumnCount()).thenAnswer(new Answer<Object>() {

      @Override
      public Object answer(InvocationOnMock invocation) throws Throwable {
        return labels.size();
      }
    });
    when(resultSetMetaData.getColumnLabel(anyInt())).thenAnswer(new Answer<String>() {

      @Override
      public String answer(InvocationOnMock invocation) throws Throwable {
        int index = (Integer) invocation.getArguments()[0];
        return labels.get(index - 1);
      }
    });
  }

  @After
  public void reset() {
    labels.clear();
    values.clear();
  }

  @Test
  public void itMapsRosettaCreatorConstructorBeanCorrectly() {
    initializeResultSet("stringProperty", "value");

    assertThat(map(RosettaCreatorConstructorBean.class).getStringProperty()).isEqualTo("value");
  }

  @Test
  public void itMapsRosettaCreatorMethodBeanCorrectly() {
    initializeResultSet("stringProperty", "value");

    assertThat(map(RosettaCreatorMethodBean.class).getStringProperty()).isEqualTo("value");
  }

  @Test
  public void itMapsRosettaNamingBeanCorrectly() {
    initializeResultSet("string_property", "value");

    assertThat(map(RosettaNamingBean.class).getStringProperty()).isEqualTo("value");
  }

  @Test
  public void itMapsRosettaValueBeanCorrectly() {
    initializeResultSet("stringProperty", "value");

    assertThat(map(RosettaValueBean.class).getStringProperty()).isEqualTo("value");
  }

  @Test
  public void itMapsStoredAsJsonBeanCorrectly() {
    String json = "{\"stringProperty\":\"value\"}";

    initializeResultSet(
            "annotatedField", json,
            "annotatedGetter", json,
            "annotatedSetter", json,
            "annotatedFieldWithDefault", json,
            "annotatedGetterWithDefault", json,
            "annotatedSetterWithDefault", json
    );

    StoredAsJsonBean bean = map(StoredAsJsonBean.class);

    assertThat(bean.getAnnotatedField().getStringProperty()).isEqualTo("value");
    assertThat(bean.getAnnotatedGetter().getStringProperty()).isEqualTo("value");
    assertThat(bean.getAnnotatedSetter().getStringProperty()).isEqualTo("value");
    assertThat(bean.getAnnotatedFieldWithDefault().getStringProperty()).isEqualTo("value");
    assertThat(bean.getAnnotatedGetterWithDefault().getStringProperty()).isEqualTo("value");
    assertThat(bean.getAnnotatedSetterWithDefault().getStringProperty()).isEqualTo("value");
  }

  @Test
  public void itMapsNullStoredAsJsonBeanCorrectly() {
    initializeResultSet(
            "annotatedField", null,
            "annotatedGetter", null,
            "annotatedSetter", null,
            "annotatedFieldWithDefault", null,
            "annotatedGetterWithDefault", null,
            "annotatedSetterWithDefault", null
    );

    StoredAsJsonBean bean = map(StoredAsJsonBean.class);

    assertThat(bean.getAnnotatedField()).isNull();
    assertThat(bean.getAnnotatedGetter()).isNull();
    assertThat(bean.getAnnotatedSetter()).isNull();
    assertThat(bean.getAnnotatedFieldWithDefault().getStringProperty()).isEqualTo("value");
    assertThat(bean.getAnnotatedGetterWithDefault().getStringProperty()).isEqualTo("value");
    assertThat(bean.getAnnotatedSetterWithDefault().getStringProperty()).isEqualTo("value");
  }

  @Test
  public void itMapsNestedBeanCorrectly() {
    initializeResultSet("inner.stringProperty", "value");

    assertThat(map(NestedBean.class).getInner().getStringProperty()).isEqualTo("value");
  }

  private void initializeResultSet(String... strings) {
    for (int i = 0; i < strings.length; i += 2) {
      labels.add(strings[i]);
      values.add(strings[i + 1]);
    }
  }

  private <T> T map(Class<T> type) {
    try {
      return new RosettaMapper<T>(type).mapRow(resultSet);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
