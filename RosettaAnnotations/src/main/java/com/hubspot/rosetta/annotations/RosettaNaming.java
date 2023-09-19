package com.hubspot.rosetta.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;

/**
 * Like @JsonNaming only limited to Rosetta mapping/binding.
 */
@Target({ElementType.ANNOTATION_TYPE, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@RosettaAnnotation
public @interface RosettaNaming {
  public Class<? extends PropertyNamingStrategy> value();
}
