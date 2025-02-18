package com.hubspot.rosetta.internal;

import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospectorPair;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

public class RosettaAnnotationIntrospectorPair extends AnnotationIntrospectorPair {

  Set<Class<? extends Annotation>> overridingAnnotations;

  public RosettaAnnotationIntrospectorPair(
    AnnotationIntrospector p,
    AnnotationIntrospector s,
    Class<? extends Annotation>... overridingAnnotations
  ) {
    super(p, s);
    this.overridingAnnotations =
      new LinkedHashSet<>(Arrays.asList(overridingAnnotations));
  }

  @Override
  public boolean hasIgnoreMarker(AnnotatedMember m) {
    if (_primary.hasIgnoreMarker(m)) {
      return true;
    }

    if (overridingAnnotations.stream().anyMatch(m::hasAnnotation)) {
      return false;
    }

    return _secondary.hasIgnoreMarker(m);
  }
}
