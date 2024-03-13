package com.hubspot.rosetta.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Like @JsonIgnore only limited to Rosetta mapping/binding
 *
 * If you want Rosetta to not include a certain element when serializing,
 * you can use this annotation.
 */
@Target(
  {
    ElementType.ANNOTATION_TYPE,
    ElementType.METHOD,
    ElementType.CONSTRUCTOR,
    ElementType.FIELD,
  }
)
@Retention(RetentionPolicy.RUNTIME)
public @interface RosettaIgnore {
}
