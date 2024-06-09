package com.hubspot.rosetta.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.immutables.annotate.InjectAnnotation;
import org.immutables.annotate.InjectAnnotation.Where;

/**
 * like @JsonProperty only limited to Rosetta mapping/binding
 *
 * For instance, if you have a property that is mcCartneySongTitle, and are using
 * a naming strategy of snake case, then the field would be expected to be mc_cartney_song_title,
 * but you might want it to be mccartney_song_title without changing the way that
 * jackson serializes/deserializes when sending through services.
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
@InjectAnnotation(type = RosettaProperty.class, target = Where.SYNTHETIC_FIELDS)
public @interface RosettaProperty {
  String USE_DEFAULT_NAME = "";

  String value() default USE_DEFAULT_NAME;
}
