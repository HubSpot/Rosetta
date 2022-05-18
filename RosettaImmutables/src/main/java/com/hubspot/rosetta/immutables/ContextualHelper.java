package com.hubspot.rosetta.immutables;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.hubspot.immutables.utils.WireSafeEnum;

class ContextualHelper {

  private ContextualHelper() {
    throw new AssertionError();
  }

  interface ExceptionHandler {
    void report(String msg) throws JsonMappingException;
  }

  public static <T> T createContextual(Supplier<JavaType> typeSupplier,
                                       Function<Class<?>, T> contextualFactory,
                                       ExceptionHandler exceptionHandler) throws JsonMappingException {
    JavaType contextualType = typeSupplier.get();
    JavaType wireSafeType = findWireSafeTypeParameter(contextualType);
    if (wireSafeType == null) {
      exceptionHandler.report("Can not handle contextualType: " + contextualType);
    } else {
      JavaType[] typeParameters = wireSafeType.findTypeParameters(WireSafeEnum.class);
      if (typeParameters.length != 1) {
        exceptionHandler.report("Can not discover enum type for: " + contextualType);
      } else if (!typeParameters[0].isEnumType()) {
        exceptionHandler.report("Can not handle non-enum type: " + typeParameters[0].getRawClass());
      } else {
        return contextualFactory.apply(typeParameters[0].getRawClass());
      }
    }

    return null;
  }

  private static JavaType findWireSafeTypeParameter(JavaType type) {
    // Contextualization of the (de)serializers happens in this case without scoping to the specific type
    // that we're being asked to handle, which means that the java type under inspection here is
    // the full type of the bean property, which could in theory contain a WireSafeEnum anywhere,
    // however in practice this is unlikely so we can get away with some specializations
    if (type == null) {
      return null;
    } else if (type.hasRawClass(WireSafeEnum.class)) {
      return type;
    } else if (
        type.hasRawClass(Optional.class) ||
        type.hasRawClass(com.google.common.base.Optional.class) ||
        type.hasRawClass(List.class)
    ) {
      return type.containedType(0);
    } else {
      return null;
    }
  }
}
