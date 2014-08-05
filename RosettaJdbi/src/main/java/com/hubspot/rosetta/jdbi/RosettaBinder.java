package com.hubspot.rosetta.jdbi;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.skife.jdbi.v2.SQLStatement;
import org.skife.jdbi.v2.sqlobject.Binder;
import org.skife.jdbi.v2.sqlobject.BinderFactory;
import org.skife.jdbi.v2.sqlobject.BindingAnnotation;

@BindingAnnotation(RosettaBinder.RosettaBinderFactory.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
public @interface RosettaBinder {
  public static class RosettaBinderFactory implements BinderFactory {
    private static final RosettaSqlBinder binder = new RosettaSqlBinder();

    @Override
    public Binder<RosettaBinder, Object> build(Annotation annotation) {
      return new Binder<RosettaBinder, Object>() {
        @Override
        public void bind(SQLStatement<?> q, RosettaBinder bind, Object arg) {
          binder.bindAll(q, arg);
        }
      };
    }
  }

}
