package com.hubspot.rosetta.internal;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import com.hubspot.rosetta.annotations.StoredAsJson;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Handles empty serialization for properties annotated with {@link com.hubspot.rosetta.annotations.StoredAsJson}
 * Can't override AnnotationIntrospector#findNullSerializer because of 2.1.x compatibility
 */
public class StoredAsJsonBeanSerializerModifier extends BeanSerializerModifier {

  @Override
  public List<BeanPropertyWriter> changeProperties(SerializationConfig config, BeanDescription beanDesc, List<BeanPropertyWriter> beanProperties) {
    for (BeanPropertyWriter beanProperty : beanProperties) {
      StoredAsJson storedAsJson = beanProperty.getAnnotation(StoredAsJson.class);
      if (storedAsJson != null && !StoredAsJson.NULL.equals(storedAsJson.empty())) {
        final JsonSerializer<Object> nullSerializer;
        if (storedAsJson.binary()) {
          nullSerializer = new ConstantBinarySerializer(storedAsJson.empty());
        } else {
          nullSerializer = new ConstantSerializer(storedAsJson.empty());
        }

        beanProperty.assignNullSerializer(nullSerializer);
      }
    }

    return super.changeProperties(config, beanDesc, beanProperties);
  }
}
