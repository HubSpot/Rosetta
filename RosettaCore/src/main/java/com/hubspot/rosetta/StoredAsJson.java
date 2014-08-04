package com.hubspot.rosetta;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.hubspot.rosetta.jackson.RosettaAnnotation;

/**
 * Indicate that a field is stored as a JSON blob that should be deserialized individually.
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@RosettaAnnotation
public @interface StoredAsJson {
  String empty() default "";
}
