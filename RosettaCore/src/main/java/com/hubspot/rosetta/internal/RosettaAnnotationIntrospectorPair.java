package com.hubspot.rosetta.internal;

import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospectorPair;

public class RosettaAnnotationIntrospectorPair extends AnnotationIntrospectorPair {

  public RosettaAnnotationIntrospectorPair(
    AnnotationIntrospector p,
    AnnotationIntrospector s
  ) {
    super(p, s);
  }

  @Override
  public boolean hasIgnoreMarker(AnnotatedMember m) {
    return _primary.hasIgnoreMarker(m);
  }
}
