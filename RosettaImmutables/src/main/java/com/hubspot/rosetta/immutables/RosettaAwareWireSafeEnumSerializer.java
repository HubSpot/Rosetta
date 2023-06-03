package com.hubspot.rosetta.immutables;

import java.io.IOException;
import java.util.Optional;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.hubspot.immutables.utils.WireSafeEnum;

public class RosettaAwareWireSafeEnumSerializer extends JsonSerializer<WireSafeEnum<?>> {

  @Override
  public void serialize(WireSafeEnum<?> value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
    Optional<? extends Enum<?>> enumValue = value.asEnum();
    if (enumValue.isPresent()) {
      serializers.defaultSerializeValue(enumValue.get(), gen);
    } else {
      serializers.reportMappingProblem(
          "Cannot serialize WireSafeEnum with unknown enum value: %s",
          value.asString());
    }
  }
}
