package com.hubspot.rosetta.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * like @JsonSetter only limited to Rosetta mapping/binding
 */
@Target(
  {
    ElementType.ANNOTATION_TYPE,
    ElementType.FIELD,
    ElementType.METHOD,
    ElementType.PARAMETER,
  }
)
@Retention(RetentionPolicy.RUNTIME)
@RosettaAnnotation
public @interface RosettaDeserializationProperty {
  String USE_DEFAULT_NAME = "";

  String value() default USE_DEFAULT_NAME;
}
