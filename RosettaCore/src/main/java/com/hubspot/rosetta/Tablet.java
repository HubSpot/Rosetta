package com.hubspot.rosetta;

import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

public interface Tablet {
  Map<String, PropertyDefinition> getFields(Class<?> unwrappedType, boolean skipJoinable);
  <T> Class<T> getUnwrappedType(Class<T> type);
  void setMapper(ObjectMapper mapper);
  void useView(Class<?> view);
  <T> T finalizeInstance(T instance);
}
