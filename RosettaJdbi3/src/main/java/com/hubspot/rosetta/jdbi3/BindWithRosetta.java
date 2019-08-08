package com.hubspot.rosetta.jdbi3;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;

import org.jdbi.v3.sqlobject.customizer.SqlStatementCustomizerFactory;
import org.jdbi.v3.sqlobject.customizer.SqlStatementCustomizingAnnotation;
import org.jdbi.v3.sqlobject.customizer.SqlStatementParameterCustomizer;

import com.hubspot.rosetta.jdbi3.BindWithRosetta.RosettaBinderFactory;
import com.hubspot.rosetta.jdbi3.RosettaStatementBinder.BindingBuilder;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
@SqlStatementCustomizingAnnotation(RosettaBinderFactory.class)
public @interface BindWithRosetta {
  String prefix() default "";

  class RosettaBinderFactory implements SqlStatementCustomizerFactory {

    @Override
    public SqlStatementParameterCustomizer createForParameter(
        Annotation annotation,
        Class<?> sqlObjectType,
        Method method,
        Parameter param,
        int index,
        Type paramType
    ) {
      String prefix = ((BindWithRosetta) annotation).prefix();
      BindingBuilder builder = RosettaStatementBinder.withPrefix(prefix);

      return (statement, arg) -> {
        builder.bind(arg).onStatement(statement);
      };
    }
  }
}
