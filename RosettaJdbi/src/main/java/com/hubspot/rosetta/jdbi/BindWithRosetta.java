package com.hubspot.rosetta.jdbi;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.hubspot.rosetta.jdbi.BindWithRosetta.RosettaBinderFactory;
import org.skife.jdbi.v2.sqlobject.Binder;
import org.skife.jdbi.v2.sqlobject.BinderFactory;
import org.skife.jdbi.v2.sqlobject.BindingAnnotation;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
@BindingAnnotation(RosettaBinderFactory.class)
public @interface BindWithRosetta {
  String value() default "";

  public static class RosettaBinderFactory implements BinderFactory {

    @Override
    public Binder<BindWithRosetta, Object> build(Annotation annotation) {
      return RosettaJdbiBinder.INSTANCE;
    }
  }
}
