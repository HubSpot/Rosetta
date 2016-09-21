package com.hubspot.rosetta.jdbi;

import com.hubspot.rosetta.beans.NestedBean;
import com.hubspot.rosetta.beans.RosettaCreatorConstructorBean;
import com.hubspot.rosetta.beans.RosettaCreatorMethodBean;
import com.hubspot.rosetta.beans.RosettaNamingBean;
import com.hubspot.rosetta.beans.RosettaValueBean;
import com.hubspot.rosetta.beans.StoredAsJsonBean;
import org.junit.Test;

import static com.hubspot.rosetta.jdbi.RosettaMapperFactory.determineGenericReturnType;
import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

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

  @Test
  public void itHandlesConcreteType() {
    Type type = determineGenericReturnType(CircularBean.class, returnTypeForMethod("concreteType"));
    assertThat(type).isEqualTo(CircularBean.class);
  }

  @Test
  public void itHandlesGenericType() {
    Type type = determineGenericReturnType(GenericBean.class, returnTypeForMethod("genericType"));
    assertThat(type).isInstanceOf(ParameterizedType.class);

    ParameterizedType genericType = (ParameterizedType) type;
    assertThat(genericType.getRawType()).isEqualTo(GenericBean.class);
    assertThat(genericType.getActualTypeArguments()).containsExactly(CircularBean.class);
  }

  @Test
  public void itHandlesPrimitives() {
    Type type = determineGenericReturnType(int.class, returnTypeForMethod("primitive"));
    assertThat(type).isEqualTo(int.class);
  }

  @Test
  public void itHandlesPrimitiveWrappers() {
    Type type = determineGenericReturnType(Integer.class, returnTypeForMethod("primitiveWrapper"));
    assertThat(type).isEqualTo(Integer.class);
  }

  @Test
  public void itHandlesListOfConcreteType() {
    Type type = determineGenericReturnType(CircularBean.class, returnTypeForMethod("listOfConcreteType"));
    assertThat(type).isEqualTo(CircularBean.class);
  }

  @Test
  public void itHandlesListOfGenericType() {
    Type type = determineGenericReturnType(GenericBean.class, returnTypeForMethod("listOfGenericType"));
    assertThat(type).isInstanceOf(ParameterizedType.class);

    ParameterizedType genericType = (ParameterizedType) type;
    assertThat(genericType.getRawType()).isEqualTo(GenericBean.class);
    assertThat(genericType.getActualTypeArguments()).containsExactly(CircularBean.class);
  }

  @Test
  public void itHandlesListOfPrimitiveWrappers() {
    Type type = determineGenericReturnType(Integer.class, returnTypeForMethod("listOfPrimitiveWrapper"));
    assertThat(type).isEqualTo(Integer.class);
  }

  @Test
  public void itExtractsTableNameBasic() {
    String sql = "SELECT * FROM table WHERE bar = baz";

    assertThat(RosettaMapperFactory.extractTableName(sql)).isEqualTo("table");
  }

  @Test
  public void itExtractsTableNameWithComma() {
    String sql = "SELECT * FROM table, other_table WHERE bar = baz";

    assertThat(RosettaMapperFactory.extractTableName(sql)).isEqualTo("table");
  }

  @Test
  public void itExtractsTableNameWithSemicolon() {
    String sql = "SELECT * FROM table;";

    assertThat(RosettaMapperFactory.extractTableName(sql)).isEqualTo("table");
  }

  private Type returnTypeForMethod(String methodName) {
    try {
      return TestDao.class.getMethod(methodName).getGenericReturnType();
    } catch (NoSuchMethodException e) {
      throw new RuntimeException(e);
    }
  }

  public interface TestDao {
    CircularBean concreteType();
    GenericBean<CircularBean> genericType();
    int primitive();
    Integer primitiveWrapper();

    List<CircularBean> listOfConcreteType();
    List<GenericBean<CircularBean>> listOfGenericType();
    List<Integer> listOfPrimitiveWrapper();
  }
}
