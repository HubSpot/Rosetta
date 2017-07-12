package com.hubspot.rosetta.beans;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.hubspot.rosetta.annotations.RosettaDeserialize;
import com.hubspot.rosetta.annotations.RosettaSerialize;

public class CustomSerializationBean {

  @RosettaSerialize(using = StringListSerializer.class)
  @RosettaDeserialize(using = StringListDeserializer.class)
  private List<String> annotatedField;
  private List<String> annotatedGetter;
  private List<String> annotatedSetter;

  private InnerSerializationBean annotatedInnerField;

  public List<String> getAnnotatedField() {
    return annotatedField;
  }

  public void setAnnotatedField(List<String> annotatedField) {
    this.annotatedField = annotatedField;
  }

  @RosettaSerialize(using = StringListSerializer.class)
  @RosettaDeserialize(using = StringListDeserializer.class)
  public List<String> getAnnotatedGetter() {
    return annotatedGetter;
  }

  public void setAnnotatedGetter(List<String> annotatedGetter) {
    this.annotatedGetter = annotatedGetter;
  }

  public List<String> getAnnotatedSetter() {
    return annotatedSetter;
  }

  @RosettaSerialize(using = StringListSerializer.class)
  @RosettaDeserialize(using = StringListDeserializer.class)
  public void setAnnotatedSetter(List<String> annotatedSetter) {
    this.annotatedSetter = annotatedSetter;
  }

  public InnerSerializationBean getAnnotatedInnerField() {
    return annotatedInnerField;
  }

  public void setAnnotatedInnerField(InnerSerializationBean annotatedInnerField) {
    this.annotatedInnerField = annotatedInnerField;
  }

  private static class StringListSerializer extends JsonSerializer<List<String>> {

    private static final Joiner JOINER = Joiner.on(";");

    @Override
    public void serialize(List<String> value,
                          JsonGenerator gen,
                          SerializerProvider serializers) throws IOException {
      gen.writeString(JOINER.join(value));
    }
  }

  private static class StringListDeserializer extends JsonDeserializer<List<String>> {

    private static final Splitter SPLITTER = Splitter.on(";");

    @Override
    public List<String> deserialize(JsonParser p,
                                    DeserializationContext ctxt) throws IOException {
      if (p.hasToken(JsonToken.VALUE_STRING)) {
        String value = p.getText();
        return SPLITTER.splitToList(value);
      }
      return Collections.emptyList();
    }
  }
}
