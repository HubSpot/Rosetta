package com.hubspot.rosetta.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.fasterxml.jackson.databind.JsonSerializer;

/**
 * Like @JsonSerialize for fields, methods and classes
 *
 * Indicate that the field, or class, should be serialized using a non-standard serializer
 */
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@RosettaAnnotation
public @interface RosettaSerialize {

  Class<? extends JsonSerializer> using();
}
