package com.hubspot.rosetta.jdbi3;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.hubspot.rosetta.RosettaBinder;
import com.hubspot.rosetta.jdbi3.BindListWithRosetta.RosettaListBinderFactory;
import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import org.jdbi.v3.sqlobject.customizer.BindList.EmptyHandling;
import org.jdbi.v3.sqlobject.customizer.SqlStatementCustomizerFactory;
import org.jdbi.v3.sqlobject.customizer.SqlStatementCustomizingAnnotation;
import org.jdbi.v3.sqlobject.customizer.SqlStatementParameterCustomizer;
import org.jdbi.v3.sqlobject.internal.ParameterUtil;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.PARAMETER })
@SqlStatementCustomizingAnnotation(RosettaListBinderFactory.class)
public @interface BindListWithRosetta {

  String value() default "";

  String field() default "";

  EmptyHandling onEmpty() default EmptyHandling.THROW;

  class RosettaListBinderFactory implements SqlStatementCustomizerFactory {

    @Override
    public SqlStatementParameterCustomizer createForParameter(
        Annotation annotation,
        Class<?> sqlObjectType,
        Method method,
        Parameter param,
        int index,
        Type paramType
    ) {
      BindListWithRosetta bindList = (BindListWithRosetta) annotation;
      String name = ParameterUtil.findParameterName(bindList.value(), param)
          .orElseThrow(() -> new UnsupportedOperationException(
              "A @BindListWithRosetta parameter was not given a name, " +
                  "and parameter name data is not present in the class file, for: " +
                  param.getDeclaringExecutable() + "::" + param
          ));

      return (stmt, arg) -> {
        ObjectMapper objectMapper = stmt.getConfig(RosettaObjectMapper.class).getObjectMapper();
        JsonNode node = objectMapper.valueToTree(arg);

        // if we got a non-null argument that isn't a list
        if (node != null && !node.isArray()) {
          throw new IllegalArgumentException("Value provided to @BindListWithRosetta was not an iterable!");
        }

        if (node == null || node.size() == 0) {
          switch (bindList.onEmpty()) {
            case VOID:
              stmt.define(name, "");
              return;
            case NULL:
              stmt.define(name, "null");
              return;
            case THROW:
              throw new IllegalArgumentException(arg == null
                  ? "argument is null; null was explicitly forbidden on this instance of BindListWithRosetta"
                  : "argument is empty; emptiness was explicitly forbidden on this instance of BindListWithRosetta");
            default:
              throw new UnsupportedOperationException("OnEmpty behavior: " + bindList.onEmpty() + "is not supported");
          }
        } else {
          List<Object> list = new ArrayList<>(node.size());
          RosettaBinder.INSTANCE.bindList((ArrayNode) node, bindList.field(), list::add);
          stmt.bindList(name, list);
        }
      };
    }
  }
}
