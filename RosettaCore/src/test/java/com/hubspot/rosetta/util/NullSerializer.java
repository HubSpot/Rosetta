package com.hubspot.rosetta.util;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.hubspot.rosetta.beans.NullPolymorphicBean;

public class NullSerializer extends StdSerializer<NullPolymorphicBean> {

  public NullSerializer() {
    super(NullPolymorphicBean.class);
  }

  @Override
  public void serialize(NullPolymorphicBean value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeNull();
  }

  @Override
  public void serializeWithType(NullPolymorphicBean value, JsonGenerator gen, SerializerProvider serializers, TypeSerializer typeSer) throws IOException {
    gen.writeNull();
  }
}
