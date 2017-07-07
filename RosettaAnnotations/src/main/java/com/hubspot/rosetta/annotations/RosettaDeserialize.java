package com.hubspot.rosetta.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.fasterxml.jackson.databind.JsonDeserializer;

/**
 * Like @JsonDeserialize but only on fields and methods
 *
 * Indicate that the field should be deserialized using a non-standard deserializer
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@RosettaAnnotation
public @interface RosettaDeserialize {

  Class<? extends JsonDeserializer> using();
}
