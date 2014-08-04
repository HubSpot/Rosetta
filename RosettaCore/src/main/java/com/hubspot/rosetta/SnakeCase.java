package com.hubspot.rosetta;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import com.hubspot.rosetta.jackson.RosettaAnnotation;

/**
 * Indicate that a class's fields should be mapped to/from "snake_case" as necessary.
 */
@Retention(RetentionPolicy.RUNTIME)
@RosettaAnnotation
public @interface SnakeCase {}
