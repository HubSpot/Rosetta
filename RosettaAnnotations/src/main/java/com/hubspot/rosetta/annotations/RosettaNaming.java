package com.hubspot.rosetta.annotations;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Like @JsonNaming only limited to Rosetta mapping/binding.
 */
@Target({ ElementType.ANNOTATION_TYPE, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@RosettaAnnotation
public @interface RosettaNaming {
  public Class<? extends PropertyNamingStrategy> value();
}
