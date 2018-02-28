package com.hubspot.rosetta.internal;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;

// modified version of the unfortunately final MethodProperty
public class StoredAsJsonMethodProperty extends SettableBeanProperty {
  private final AnnotatedMethod annotatedMethod;
  private final transient Method setter;

  public StoredAsJsonMethodProperty(BeanPropertyDefinition propDef,
                                    JavaType type,
                                    TypeDeserializer typeDeser,
                                    Annotations contextAnnotations,
                                    AnnotatedMethod method) {
    super(propDef, type, typeDeser, contextAnnotations);
    annotatedMethod = method;
    setter = method.getAnnotated();
  }

  protected StoredAsJsonMethodProperty(StoredAsJsonMethodProperty src, JsonDeserializer<?> deser) {
    super(src, deser);
    annotatedMethod = src.annotatedMethod;
    setter = src.setter;
  }

  protected StoredAsJsonMethodProperty(StoredAsJsonMethodProperty src, PropertyName newName) {
    super(src, newName);
    annotatedMethod = src.annotatedMethod;
    setter = src.setter;
  }

  @Override
  public StoredAsJsonMethodProperty withName(PropertyName newName) {
    return new StoredAsJsonMethodProperty(this, newName);
  }

  @Override
  public StoredAsJsonMethodProperty withValueDeserializer(JsonDeserializer<?> deser) {
    return new StoredAsJsonMethodProperty(this, deser);
  }

  @Override
  public <A extends Annotation> A getAnnotation(Class<A> acls) {
    return (annotatedMethod == null) ? null : annotatedMethod.getAnnotation(acls);
  }

  @Override public AnnotatedMember getMember() {
    return annotatedMethod;
  }

  @Override
  public void deserializeAndSet(JsonParser p, DeserializationContext ctxt, Object instance) throws IOException {
    Object value = deserializeStoredAsJson(p, ctxt);
    try {
      setter.invoke(instance, value);
    } catch (Exception e) {
      _throwAsIOE(p, e, value);
    }
  }

  @Override
  public Object deserializeSetAndReturn(JsonParser p, DeserializationContext ctxt, Object instance) throws IOException {
    Object value = deserializeStoredAsJson(p, ctxt);
    try {
      Object result = setter.invoke(instance, value);
      return (result == null) ? instance : result;
    } catch (Exception e) {
      _throwAsIOE(p, e, value);
      return null;
    }
  }

  @Override
  public final void set(Object instance, Object value) throws IOException {
    try {
      setter.invoke(instance, value);
    } catch (Exception e) {
      _throwAsIOE(e, value);
    }
  }

  @Override
  public Object setAndReturn(Object instance, Object value) throws IOException {
    try {
      Object result = setter.invoke(instance, value);
      return (result == null) ? instance : result;
    } catch (Exception e) {
      _throwAsIOE(e, value);
      return null;
    }
  }

  private final Object deserializeStoredAsJson(JsonParser p, DeserializationContext ctxt) throws IOException
  {
    JsonToken t = p.getCurrentToken();
    if (t == JsonToken.VALUE_NULL) {
      return _valueDeserializer.getNullValue(ctxt);
    }

    if (_valueTypeDeserializer != null) {
      return _valueDeserializer.deserializeWithType(p, ctxt, _valueTypeDeserializer);
    }

    return _valueDeserializer.deserialize(p, ctxt);
  }
}
