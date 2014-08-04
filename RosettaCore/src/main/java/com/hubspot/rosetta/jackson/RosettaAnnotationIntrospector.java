package com.hubspot.rosetta.jackson;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.NopAnnotationIntrospector;
import com.fasterxml.jackson.databind.util.NameTransformer;
import com.hubspot.rosetta.Joinable;
import com.hubspot.rosetta.RosettaCreator;
import com.hubspot.rosetta.RosettaValue;
import com.hubspot.rosetta.SnakeCase;
import com.hubspot.rosetta.StoredAsJson;

public class RosettaAnnotationIntrospector extends NopAnnotationIntrospector {
  private static final long serialVersionUID = 1L;
  private static final String prefixFormat = "%s.";
  private static final PropertyNamingStrategy NAMING_STRATEGY = new RosettaNamingStrategy();

  @Override
  public Object findNamingStrategy(AnnotatedClass klass) {
    if (klass.hasAnnotation(SnakeCase.class)) {
      return PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES;
    }
    return NAMING_STRATEGY;
  }

  @Override
  public JsonDeserializer<?> findDeserializer(Annotated am) {
    Class<?> klass = findAppropriateType(am);
    if (klass != null) {
      StoredAsJson storedAsJson = am.getAnnotation(StoredAsJson.class);
      if (storedAsJson != null) {
        return StoredAsJsonDeserializer.forType(klass, storedAsJson.empty());
      }
    }
    return null;
  }

  @Override
  public JsonSerializer<?> findSerializer(Annotated am) {
    Class<?> klass = findAppropriateType(am);
    if (klass != null) {
      StoredAsJson storedAsJson = am.getAnnotation(StoredAsJson.class);
      if (storedAsJson != null) {
        return StoredAsJsonSerializer.forType(klass, storedAsJson.empty());
      }
    }
    return null;
  }

  @Override
  public JsonSerializer<?> findNullSerializer(Annotated am) {
    Class<?> klass = findAppropriateType(am);
    if (klass != null) {
      StoredAsJson storedAsJson = am.getAnnotation(StoredAsJson.class);
      if (storedAsJson != null) {
        return StoredAsJsonSerializer.forType(klass, storedAsJson.empty());
      }
    }
    return null;
  }

  @Override
  public boolean hasAsValueAnnotation(AnnotatedMethod am) {
    RosettaValue ann = am.getAnnotation(RosettaValue.class);
    return ann != null;
  }

  @Override
  public boolean hasCreatorAnnotation(Annotated a) {
    return a.hasAnnotation(RosettaCreator.class);
  }

  @Override
  public NameTransformer findUnwrappingNameTransformer(AnnotatedMember am) {
    Joinable ann = am.getAnnotation(Joinable.class);
    if (ann == null) {
      return null;
    }
    if (!ann.as().equals("")) {
      return NameTransformer.simpleTransformer(String.format(prefixFormat, ann.as()), "");
    } else {
      PropertyName name = findNameForSerialization(am);
      if (name != null) {
        return NameTransformer.simpleTransformer(name.getSimpleName(), "");
      } else {
        String nameStr = am.getName();
        if (nameStr.startsWith("set") || nameStr.startsWith("get")) {
          nameStr = nameStr.substring(3);
          if (!nameStr.isEmpty()) {
            nameStr = nameStr.substring(0, 1).toLowerCase() + nameStr.substring(1);
          }
        }
        return NameTransformer.simpleTransformer(String.format(prefixFormat, nameStr), "");
      }
    }
  }

  private Class<?> findAppropriateType(Annotated am) {
    Class<?> klass = null;
    if (am instanceof AnnotatedField) {
      klass = ((AnnotatedField) am).getAnnotated().getType();
    } else if (am instanceof AnnotatedMethod) {
      klass = ((AnnotatedMethod) am).getAnnotated().getReturnType();
      AnnotatedMethod a = (AnnotatedMethod) am;
      if (!Object.class.isAssignableFrom(klass) || a.getDeclaringClass().isAssignableFrom(klass)) {
        Class<?>[] ptypes = a.getAnnotated().getParameterTypes();
        if (ptypes.length > 0) {
          klass = ptypes[0];
        }
      }
    }
    if (klass != null && Object.class.isAssignableFrom(klass)) {
      return klass;
    }
    return null;
  }

  @Override
  public Version version() {
    return new Version(1, 0, 0, null, null, null);
  }

}
