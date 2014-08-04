package com.hubspot.rosetta;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import com.hubspot.rosetta.jackson.RosettaAnnotation;

@RosettaAnnotation
@Retention(RetentionPolicy.RUNTIME)
public @interface ColumnName {
  String value();
}
