package com.hubspot.rosetta.annotations;

import com.fasterxml.jackson.databind.JsonDeserializer;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Like @JsonDeserialize for fields, methods and classes
 *
 * Indicate that the field, or class, should be deserialized using a non-standard deserializer
 */
@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@RosettaAnnotation
public @interface RosettaDeserialize {
  Class<? extends JsonDeserializer> using();
}
