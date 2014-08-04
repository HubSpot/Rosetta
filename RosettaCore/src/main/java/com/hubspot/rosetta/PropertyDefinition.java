package com.hubspot.rosetta;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.google.common.base.Optional;

public class PropertyDefinition {

  private final BeanPropertyDefinition definition;
  private final Class<?> fieldType;
  private final Type genericType;
  private Optional<PropertyDefinition> parent = Optional.absent();

  public PropertyDefinition(BeanPropertyDefinition definition) {
    this(definition, null);
  }

  public PropertyDefinition(BeanPropertyDefinition definition, BeanPropertyDefinition parent) {
    this.definition = definition;
    if (parent != null) {
      this.parent = Optional.of(new PropertyDefinition(parent));
    }
    if (definition.hasField()) {
      fieldType = definition.getField().getAnnotated().getType();
      genericType = definition.getField().getGenericType();
    } else if (definition.hasGetter()) {
      fieldType = definition.getGetter().getRawReturnType();
      genericType = definition.getGetter().getGenericReturnType();
    } else if (definition.hasSetter()) {
      fieldType = definition.getSetter().getRawParameterType(0);
      genericType = definition.getSetter().getGenericParameterType(0);
    }
    else throw new RuntimeException("Definition incomplete!");
  }

  public Boolean hasParent() {
    return parent.isPresent();
  }

  public PropertyDefinition getParent() {
    return parent.get();
  }

  public void setParent(BeanPropertyDefinition parent) {
    this.parent = Optional.of(new PropertyDefinition(parent));
  }

  public BeanPropertyDefinition getDefinition() {
    return definition;
  }

  public Class<?> getType() {
    return fieldType;
  }

  public Type getGenericType() {
    return genericType;
  }

  public Object getValue(Object fromObject) {
    return definition.getAccessor().getValue(fromObject);
  }

  public boolean couldMap() {
    return definition.couldDeserialize();
  }

  public boolean couldBind() {
    return definition.couldSerialize();
  }

  public <T extends Annotation> T getAnnotation(Class<T> type) {
    if (definition.hasField() && definition.getField().getAnnotation(type) != null) {
      return definition.getField().getAnnotation(type);
    } else if (definition.hasGetter() && definition.getGetter().getAnnotation(type) != null) {
      return definition.getGetter().getAnnotation(type);
    } else if (definition.hasSetter() && definition.getSetter().getAnnotation(type) != null) {
      return definition.getSetter().getAnnotation(type);
    }
    return null;
  }

  public boolean hasAnnotation(Class<? extends Annotation> type) {
    return getAnnotation(type) != null;
  }

  public Tablet getTablet() {
    return Rosetta.tabletForType(getType());
  }

}
