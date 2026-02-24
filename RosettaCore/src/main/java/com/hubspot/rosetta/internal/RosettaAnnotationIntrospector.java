package com.hubspot.rosetta.internal;

import com.fasterxml.jackson.annotation.JsonCreator.Mode;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonInclude.Value;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.NopAnnotationIntrospector;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerFactory;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import com.fasterxml.jackson.databind.ser.Serializers;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.hubspot.rosetta.annotations.RosettaCreator;
import com.hubspot.rosetta.annotations.RosettaDeserializationProperty;
import com.hubspot.rosetta.annotations.RosettaDeserialize;
import com.hubspot.rosetta.annotations.RosettaIgnore;
import com.hubspot.rosetta.annotations.RosettaNaming;
import com.hubspot.rosetta.annotations.RosettaProperty;
import com.hubspot.rosetta.annotations.RosettaSerializationProperty;
import com.hubspot.rosetta.annotations.RosettaSerialize;
import com.hubspot.rosetta.annotations.RosettaValue;
import com.hubspot.rosetta.annotations.StoredAsJson;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

public class RosettaAnnotationIntrospector extends NopAnnotationIntrospector {

  private static final long serialVersionUID = 1L;

  private final ObjectMapper objectMapper;
  private volatile ObjectMapper storedAsJsonMapper;

  public RosettaAnnotationIntrospector(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
    this.storedAsJsonMapper = null;
  }

  @Override
  public Object findNamingStrategy(AnnotatedClass ac) {
    RosettaNaming ann = ac.getAnnotation(RosettaNaming.class);
    return ann == null ? null : ann.value();
  }

  @Override
  @SuppressWarnings("unchecked")
  public JsonSerializer<?> findSerializer(Annotated a) {
    StoredAsJson storedAsJson = a.getAnnotation(StoredAsJson.class);
    RosettaSerialize rosettaSerialize = a.getAnnotation(RosettaSerialize.class);
    if (storedAsJson != null && rosettaSerialize != null) {
      throw new IllegalArgumentException(
        "Cannot have @StoredAsJson as well as @RosettaSerialize annotations on the same entry"
      );
    }
    if (storedAsJson != null) {
      Class<?> type = a.getRawType();
      return storedAsJson.binary()
        ? new StoredAsJsonBinarySerializer(type, getStoredAsJsonMapper(), objectMapper)
        : new StoredAsJsonSerializer(type, getStoredAsJsonMapper(), objectMapper);
    }

    if (rosettaSerialize != null) {
      Class<? extends JsonSerializer> klass = rosettaSerialize.using();
      if (klass != JsonSerializer.None.class) {
        return ClassUtil.createInstance(
          klass,
          getStoredAsJsonMapper().getSerializationConfig().canOverrideAccessModifiers()
        );
      }
    }
    return null;
  }

  @Override
  @SuppressWarnings("unchecked")
  public JsonDeserializer<?> findDeserializer(Annotated a) {
    StoredAsJson storedAsJson = a.getAnnotation(StoredAsJson.class);
    RosettaDeserialize rosettaDeserialize = a.getAnnotation(RosettaDeserialize.class);
    if (storedAsJson != null && rosettaDeserialize != null) {
      throw new IllegalArgumentException(
        "Cannot have @StoredAsJson as well as @RosettaDeserialize annotations on the same entry"
      );
    }
    if (storedAsJson != null) {
      if (a instanceof AnnotatedMethod) {
        a = getAnnotatedTypeFromAnnotatedMethod((AnnotatedMethod) a);
      }

      String empty = StoredAsJson.NULL.equals(storedAsJson.empty())
        ? "null"
        : storedAsJson.empty();
      return new StoredAsJsonDeserializer(
        a.getRawType(),
        a.getType(),
        empty,
        getStoredAsJsonMapper(),
        objectMapper
      );
    }

    if (rosettaDeserialize != null) {
      Class<? extends JsonDeserializer> klass = rosettaDeserialize.using();
      if (klass != JsonDeserializer.None.class) {
        return ClassUtil.createInstance(
          klass,
          getStoredAsJsonMapper().getDeserializationConfig().canOverrideAccessModifiers()
        );
      }
    }

    return null;
  }

  @Override
  public Mode findCreatorAnnotation(MapperConfig<?> config, Annotated ann) {
    if (ann.hasAnnotation(RosettaCreator.class)) {
      return Mode.DEFAULT;
    }
    return null;
  }

  @Override
  public PropertyName findNameForSerialization(Annotated a) {
    return getFirstNonEmpty(
        () -> findRosettaGetterName(a),
        () -> findRosettaPropertyName(a),
        () -> Optional.ofNullable(super.findNameForSerialization(a))
      )
      .orElse(null);
  }

  @Override
  public PropertyName findNameForDeserialization(Annotated a) {
    return getFirstNonEmpty(
        () -> findRosettaSetterName(a),
        () -> findRosettaPropertyName(a),
        () -> Optional.ofNullable(super.findNameForDeserialization(a))
      )
      .orElse(null);
  }

  @Override
  public Value findPropertyInclusion(Annotated a) {
    return Value.construct(Include.ALWAYS, Include.ALWAYS);
  }

  @Override
  public boolean hasIgnoreMarker(AnnotatedMember m) {
    return m.hasAnnotation(RosettaIgnore.class);
  }

  @Override
  @SuppressFBWarnings("NP_BOOLEAN_RETURN_NULL")
  public Boolean hasAsValue(Annotated a) {
    if (a.hasAnnotation(RosettaIgnore.class)) {
      // The super method can return null, so we can't use && here
      return false;
    } else {
      return a.hasAnnotation(RosettaValue.class) ? true : null;
    }
  }

  @Override
  public Version version() {
    return Version.unknownVersion();
  }

  private Optional<PropertyName> findRosettaPropertyName(Annotated a) {
    return Optional
      .ofNullable(a.getAnnotation(RosettaProperty.class))
      .map(annotation ->
        annotation.value().isEmpty()
          ? PropertyName.USE_DEFAULT
          : new PropertyName(annotation.value())
      );
  }

  private Optional<PropertyName> findRosettaGetterName(Annotated a) {
    return Optional
      .ofNullable(a.getAnnotation(RosettaSerializationProperty.class))
      .map(annotation ->
        annotation.value().isEmpty()
          ? PropertyName.USE_DEFAULT
          : new PropertyName(annotation.value())
      );
  }

  private Optional<PropertyName> findRosettaSetterName(Annotated a) {
    return Optional
      .ofNullable(a.getAnnotation(RosettaDeserializationProperty.class))
      .map(annotation ->
        annotation.value().isEmpty()
          ? PropertyName.USE_DEFAULT
          : new PropertyName(annotation.value())
      );
  }

  private static Annotated getAnnotatedTypeFromAnnotatedMethod(AnnotatedMethod a) {
    if (a.getParameterCount() > 0) {
      return a.getParameter(0);
    } else if (a.hasReturnType()) {
      return a;
    } else {
      throw new IllegalArgumentException(
        "Cannot have @StoredAsJson on a method with no parameters AND no arguments"
      );
    }
  }

  private <T> Optional<T> getFirstNonEmpty(Supplier<Optional<T>>... suppliers) {
    for (Supplier<Optional<T>> supplier : suppliers) {
      Optional<T> maybeValue = supplier.get();
      if (maybeValue.isPresent()) {
        return maybeValue;
      }
    }
    return Optional.empty();
  }

  private ObjectMapper getStoredAsJsonMapper() {
    if (storedAsJsonMapper == null) {
      synchronized (this) {
        if (storedAsJsonMapper == null) {
          storedAsJsonMapper = createSafeMapper(objectMapper);
        }
      }
    }

    return storedAsJsonMapper;
  }

  static ObjectMapper createSafeMapper(ObjectMapper sourceMapper) {
    AtomicReference<ObjectMapper> safeMapperRef = new AtomicReference<>();

    NopAnnotationIntrospector rosettaOnly = new RosettaAnnotationIntrospector(
      sourceMapper
    ) {
      @Override
      public Value findPropertyInclusion(Annotated a) {
        if (a instanceof AnnotatedClass) {
          return Value.construct(Include.ALWAYS, Include.ALWAYS);
        }
        return null;
      }

      @Override
      @SuppressWarnings("unchecked")
      public JsonSerializer<?> findSerializer(Annotated a) {
        RosettaSerialize rosettaSerialize = a.getAnnotation(RosettaSerialize.class);
        if (rosettaSerialize != null) {
          Class<? extends JsonSerializer> klass = rosettaSerialize.using();
          if (klass != JsonSerializer.None.class) {
            return ClassUtil.createInstance(
              klass,
              sourceMapper.getSerializationConfig().canOverrideAccessModifiers()
            );
          }
        }
        StoredAsJson storedAsJson = a.getAnnotation(StoredAsJson.class);
        if (storedAsJson != null) {
          ObjectMapper safeMapper = safeMapperRef.get();
          Class<?> type = a.getRawType();
          return new StoredAsJsonSerializer(type, safeMapper, safeMapper);
        }
        return null;
      }

      @Override
      @SuppressWarnings("unchecked")
      public JsonDeserializer<?> findDeserializer(Annotated a) {
        RosettaDeserialize rosettaDeserialize = a.getAnnotation(RosettaDeserialize.class);
        if (rosettaDeserialize != null) {
          Class<? extends JsonDeserializer> klass = rosettaDeserialize.using();
          if (klass != JsonDeserializer.None.class) {
            return ClassUtil.createInstance(
              klass,
              sourceMapper.getDeserializationConfig().canOverrideAccessModifiers()
            );
          }
        }
        StoredAsJson storedAsJson = a.getAnnotation(StoredAsJson.class);
        if (storedAsJson != null) {
          Annotated effective = a;
          if (a instanceof AnnotatedMethod) {
            effective = getAnnotatedTypeFromAnnotatedMethod((AnnotatedMethod) a);
          }
          String empty = StoredAsJson.NULL.equals(storedAsJson.empty())
            ? "null"
            : storedAsJson.empty();
          ObjectMapper safeMapper = safeMapperRef.get();
          return new StoredAsJsonDeserializer(
            effective.getRawType(),
            effective.getType(),
            empty,
            safeMapper,
            safeMapper
          );
        }
        return null;
      }
    };

    AnnotationIntrospector existing = sourceMapper
      .getSerializationConfig()
      .getAnnotationIntrospector();
    AnnotationIntrospector secondary = extractSecondaryIntrospector(existing);

    ObjectMapper mapper = sourceMapper
      .copy()
      .setAnnotationIntrospector(AnnotationIntrospector.pair(rosettaOnly, secondary));
    mapper.setSerializerFactory(
      rebuildFactoryWithTextOnlyNullSerializers(mapper.getSerializerFactory())
    );
    mapper.setSerializerProvider(new DefaultSerializerProvider.Impl());
    safeMapperRef.set(mapper);
    return mapper;
  }

  private static SerializerFactory rebuildFactoryWithTextOnlyNullSerializers(
    SerializerFactory original
  ) {
    if (!(original instanceof BeanSerializerFactory)) {
      return original;
    }
    BeanSerializerFactory bsf = (BeanSerializerFactory) original;
    BeanSerializerFactory result = BeanSerializerFactory.instance;
    for (Serializers s : bsf.getFactoryConfig().serializers()) {
      result = (BeanSerializerFactory) result.withAdditionalSerializers(s);
    }
    for (BeanSerializerModifier m : bsf.getFactoryConfig().serializerModifiers()) {
      if (m instanceof StoredAsJsonBeanSerializerModifier) {
        result =
          (BeanSerializerFactory) result.withSerializerModifier(
            new BeanSerializerModifier() {
              @Override
              public List<BeanPropertyWriter> changeProperties(
                SerializationConfig config,
                BeanDescription beanDesc,
                List<BeanPropertyWriter> beanProperties
              ) {
                for (BeanPropertyWriter beanProperty : beanProperties) {
                  StoredAsJson storedAsJson = beanProperty.getAnnotation(
                    StoredAsJson.class
                  );
                  if (
                    storedAsJson != null &&
                    !StoredAsJson.NULL.equals(storedAsJson.empty())
                  ) {
                    beanProperty.assignNullSerializer(
                      new ConstantSerializer(storedAsJson.empty())
                    );
                  }
                }
                return beanProperties;
              }
            }
          );
      } else {
        result = (BeanSerializerFactory) result.withSerializerModifier(m);
      }
    }
    return result;
  }

  private static AnnotationIntrospector extractSecondaryIntrospector(
    AnnotationIntrospector existing
  ) {
    Collection<AnnotationIntrospector> all = existing.allIntrospectors();
    List<AnnotationIntrospector> nonRosetta = new ArrayList<>();
    for (AnnotationIntrospector ai : all) {
      if (!(ai instanceof RosettaAnnotationIntrospector)) {
        nonRosetta.add(ai);
      }
    }
    if (nonRosetta.isEmpty()) {
      return new JacksonAnnotationIntrospector();
    }
    AnnotationIntrospector result = nonRosetta.get(nonRosetta.size() - 1);
    for (int i = nonRosetta.size() - 2; i >= 0; i--) {
      result = AnnotationIntrospector.pair(nonRosetta.get(i), result);
    }
    return result;
  }
}
