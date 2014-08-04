package com.hubspot.rosetta.tablets;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.hubspot.rosetta.Joinable;
import com.hubspot.rosetta.PropertyDefinition;
import com.hubspot.rosetta.Rosetta;
import com.hubspot.rosetta.Tablet;

/**
 * A tablet designed for standard Java Beans.
 *
 * @author tdavis
 */
public class BeanTablet implements Tablet {

  private ObjectMapper mapper;
  private final Map<Class<?>, Map<String, PropertyDefinition>> descriptionCache = new ConcurrentHashMap<Class<?>, Map<String, PropertyDefinition>>();
  private Class<?> view;

  @Override
  public Map<String, PropertyDefinition> getFields(Class<?> type, boolean skipJoinable) {
    final Map<String, PropertyDefinition> names = new HashMap<String, PropertyDefinition>();
    PropertyDefinition propDef;
    String mappedName;

    if (descriptionCache.containsKey(type)) {
      return descriptionCache.get(type);
    }

    for (BeanPropertyDefinition def : introspect(type).findProperties()) {
      propDef = new PropertyDefinition(def);
      mappedName = def.getName();
      if (!skipJoinable && propDef.hasAnnotation(Joinable.class)) {
        String prefix = propDef.getAnnotation(Joinable.class).as();
        prefix = prefix.isEmpty() ? def.getName() : prefix;
        for (final Map.Entry<String, PropertyDefinition> s : Rosetta.tabletForType(propDef.getType()).getFields(propDef.getType(), !skipJoinable).entrySet()) {
          s.getValue().setParent(def);
          names.put(String.format("%s.%s", prefix, s.getKey()), s.getValue());
        }
      } else {
        names.put(mappedName, new PropertyDefinition(def));
      }

    }

    descriptionCache.put(type, names);
    return names;
  }

  private BeanDescription introspect(Class<?> type) {
    final JavaType javaType;
    SerializationConfig sc = mapper.getSerializationConfig();
    if (view != null) {
      sc = sc.withView(view);
    }

    type = getUnwrappedType(type);
    javaType = mapper.getTypeFactory().constructType(type);
    return sc.introspect(javaType);
  }

  @Override
  public <T> Class<T> getUnwrappedType(Class<T> type) {
    return type;
  }

  @Override
  public void setMapper(ObjectMapper mapper) {
    this.mapper = mapper;
  }

  @Override
  public <T> T finalizeInstance(T instance) {
    return instance;
  }

  @Override
  public void useView(Class<?> view) {
    this.view = view;
  }

}
