package com.hubspot.rosetta.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Like @JsonCreator only limited to Rosetta mapping/binding.
 *
 * For instance, you may want to let Rosetta know that a certain field has a given internal representation, but not
 * expose that same representation to the outside via normal JSON serialization.
 *
 */
@Target({ElementType.ANNOTATION_TYPE, ElementType.METHOD, ElementType.CONSTRUCTOR})
@Retention(RetentionPolicy.RUNTIME)
@RosettaAnnotation
public @interface RosettaCreator {
}
