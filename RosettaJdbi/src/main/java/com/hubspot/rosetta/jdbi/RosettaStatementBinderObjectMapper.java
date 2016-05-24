package com.hubspot.rosetta.jdbi;

import com.hubspot.rosetta.Rosetta;
import org.skife.jdbi.v2.SQLStatement;
import org.skife.jdbi.v2.sqlobject.SqlStatementCustomizer;
import org.skife.jdbi.v2.sqlobject.SqlStatementCustomizerFactory;
import org.skife.jdbi.v2.sqlobject.SqlStatementCustomizingAnnotation;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Method;
import java.sql.SQLException;

@Retention(RetentionPolicy.RUNTIME)
@SqlStatementCustomizingAnnotation(RosettaStatementBinderObjectMapper.CustomizerFactory.class)
public @interface RosettaStatementBinderObjectMapper {

    String value() default "";

    final class CustomizerFactory implements SqlStatementCustomizerFactory {

        @Override
        public SqlStatementCustomizer createForMethod(Annotation annotation, Class sqlObjectType, Method method) {
            return getCustomizer(annotation);
        }

        @Override
        public SqlStatementCustomizer createForType(Annotation annotation, Class sqlObjectType) {
            return getCustomizer(annotation);
        }

        @Override
        public SqlStatementCustomizer createForParameter(
                Annotation annotation, Class sqlObjectType, Method method, Object arg) {
            return getCustomizer(annotation);
        }

        private SqlStatementCustomizer getCustomizer(Annotation annotation) {

            RosettaStatementBinderObjectMapper rosettaResultSetObjectMapper = (RosettaStatementBinderObjectMapper) annotation;
            final String mapperName = rosettaResultSetObjectMapper.value();

            return new SqlStatementCustomizer() {

                @Override
                public void apply(SQLStatement q) throws SQLException {

                    if (mapperName != null && !mapperName.isEmpty()) {
                        q.define(RosettaObjectMapperOverride.STATEMENT_BINDER_ATTRIBUTE_NAME,
                                Rosetta.getNamedMapper(mapperName)
                        );
                    }

                }
            };
        }
    }
}
