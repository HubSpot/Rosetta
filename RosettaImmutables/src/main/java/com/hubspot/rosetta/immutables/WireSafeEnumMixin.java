package com.hubspot.rosetta.immutables;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(using = RosettaAwareWireSafeEnumSerializer.class)
@JsonDeserialize(using = RosettaAwareWireSafeEnumDeserializer.class)
public class WireSafeEnumMixin {
}
