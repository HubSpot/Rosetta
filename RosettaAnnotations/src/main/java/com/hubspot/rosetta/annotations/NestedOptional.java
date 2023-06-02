package com.hubspot.rosetta.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicate that a field is a nested optional and deserialization will follow the nested optional rules.
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@RosettaAnnotation
public @interface NestedOptional {
}
