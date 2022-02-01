package com.hubspot.rosetta.immutables;

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
    if (contextualType == null || !contextualType.hasRawClass(WireSafeEnum.class)) {
      exceptionHandler.report("Can not handle contextualType: " + contextualType);
    } else {
      JavaType[] typeParameters = contextualType.findTypeParameters(WireSafeEnum.class);
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
}
