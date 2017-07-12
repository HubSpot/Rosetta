package com.hubspot.rosetta.beans;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.hubspot.rosetta.annotations.RosettaDeserialize;
import com.hubspot.rosetta.annotations.RosettaSerialize;

@RosettaSerialize(using = InnerSerializationBean.InnerBeanSeralizer.class)
@RosettaDeserialize(using = InnerSerializationBean.InnerBeanDeserializer.class)
public class InnerSerializationBean {

  private final String prefix;
  private final String suffix;

  public InnerSerializationBean(String prefix, String suffix) {
    this.prefix = prefix;
    this.suffix = suffix;
  }

  public String getPrefix() {
    return prefix;
  }

  public String getSuffix() {
    return suffix;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    InnerSerializationBean that = (InnerSerializationBean) o;

    if (prefix != null ? !prefix.equals(that.prefix) : that.prefix != null) return false;
    return suffix != null ? suffix.equals(that.suffix) : that.suffix == null;
  }

  public static class InnerBeanSeralizer extends JsonSerializer<InnerSerializationBean> {

    @Override
    public void serialize(InnerSerializationBean value,
                          JsonGenerator gen,
                          SerializerProvider serializers) throws IOException {
      gen.writeString(String.format("%s:%s", value.getPrefix(), value.getSuffix()));
    }
  }

  public static class InnerBeanDeserializer extends JsonDeserializer<InnerSerializationBean> {

    @Override
    public InnerSerializationBean deserialize(JsonParser p,
                                              DeserializationContext ctxt) throws IOException {
      if (p.hasToken(JsonToken.VALUE_STRING)) {
        String[] values = p.getText().split(":");
        return new InnerSerializationBean(values[0], values[1]);
      }
      return null;
    }
  }
}
