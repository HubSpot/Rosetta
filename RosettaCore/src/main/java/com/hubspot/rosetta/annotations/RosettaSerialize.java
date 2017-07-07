package com.hubspot.rosetta.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.fasterxml.jackson.databind.JsonSerializer;

/**
 * Like @JsonSerialize but only on fields and methods
 *
 * Indicate that the field should be serialized using a non-standard serializer
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@RosettaAnnotation
public @interface RosettaSerialize {

  Class<? extends JsonSerializer> using();
}
