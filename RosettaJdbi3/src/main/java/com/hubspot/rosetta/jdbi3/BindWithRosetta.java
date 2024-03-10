package com.hubspot.rosetta.jdbi3;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hubspot.rosetta.RosettaBinder;
import com.hubspot.rosetta.jdbi3.BindWithRosetta.RosettaBinderFactory;
import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import org.jdbi.v3.sqlobject.customizer.SqlStatementCustomizerFactory;
import org.jdbi.v3.sqlobject.customizer.SqlStatementCustomizingAnnotation;
import org.jdbi.v3.sqlobject.customizer.SqlStatementParameterCustomizer;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.PARAMETER })
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
      return (stmt, arg) -> {
        ObjectMapper objectMapper = stmt
          .getConfig(RosettaObjectMapper.class)
          .getObjectMapper();

        JsonNode node = objectMapper.valueToTree(arg);
        String prefix = ((BindWithRosetta) annotation).prefix();

        if (node.isValueNode() || node.isArray()) {
          node =
            objectMapper.createObjectNode().set(prefix.isEmpty() ? "it" : prefix, node);
          prefix = "";
        }

        Map<String, Object> namedValues = new HashMap<>();
        RosettaBinder.INSTANCE.bind(prefix, node, namedValues::put);

        stmt.bindMap(namedValues);
      };
    }
  }
}
