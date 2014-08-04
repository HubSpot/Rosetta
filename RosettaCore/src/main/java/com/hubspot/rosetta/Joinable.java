package com.hubspot.rosetta;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import com.hubspot.rosetta.jackson.RosettaAnnotation;

/**
 * To use a field's own members in join query, annotate the field with this interface.
 *
 * TODO: Example
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@RosettaAnnotation
public @interface Joinable {
  String as() default "";
}
