package com.hubspot.rosetta;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import com.hubspot.rosetta.jackson.RosettaAnnotation;

/**
 * Like {@link JsonValue} only limited to Rosetta mapping/binding.
 *
 * For instance, you may want to let Rosetta know that a certain field has a given internal representation, but not
 * expose that same representation to the outside via normal JSON serialization.
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@RosettaAnnotation
public @interface RosettaValue {
}
