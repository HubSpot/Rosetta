package com.hubspot.rosetta.jackson;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.hubspot.rosetta.ColumnName;

public class RosettaNamingStrategy extends PropertyNamingStrategy {

  private static final long serialVersionUID = 1L;

  @Override
  public String nameForField(MapperConfig<?> config, AnnotatedField field, String defaultName) {
    return convert(field, defaultName);
  }

  @Override
  public String nameForGetterMethod(MapperConfig<?> config, AnnotatedMethod getter, String defaultName) {
    return convert(getter, defaultName);
  }

  @Override
  public String nameForSetterMethod(MapperConfig<?> config, AnnotatedMethod setter, String defaultName) {
    return convert(setter, defaultName);
  }

  @Override
  public String nameForConstructorParameter(MapperConfig<?> config, AnnotatedParameter param, String defaultName) {
    return convert(param, defaultName);
  }

  private String convert(AnnotatedMember member, String defaultName) {
    ColumnName ann = member.getAnnotation(ColumnName.class);
    if (ann != null) {
      return ann.value();
    }
    return defaultName;
  }

}
