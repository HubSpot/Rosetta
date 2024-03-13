package com.hubspot.rosetta;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.hubspot.rosetta.beans.ServiceLoaderBean;
import java.io.IOException;

public class TestModule extends SimpleModule {

  public TestModule() {
    addSerializer(
      new StdSerializer<ServiceLoaderBean>(ServiceLoaderBean.class) {
        @Override
        public void serialize(
          ServiceLoaderBean value,
          JsonGenerator jgen,
          SerializerProvider provider
        ) throws IOException {
          jgen.writeStartObject();
          jgen.writeNumberField("id_value", value.getId());
          jgen.writeStringField("name_value", value.getName());
          jgen.writeEndObject();
        }
      }
    );
  }
}
