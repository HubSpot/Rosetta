package com.hubspot.rosetta.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * like @JsonProperty only limited to Rosetta mapping/binding
 *
 * For instance, if you have a property that is hubSpotCustomerName, and are using
 * a naming strategy of snake case, then the field would be expected to be hub_spot_customer_name,
 * but you might want it to be hubspot_customer_name without changing the way that
 * jackson serializes/deserializes when sending through services.
 */
@Target({ElementType.ANNOTATION_TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@RosettaAnnotation
public @interface RosettaProperty {
  String value();
}
