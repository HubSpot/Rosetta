package com.hubspot.rosetta.jdbi.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.hubspot.rosetta.jdbi.beans.NameBean;

import java.io.IOException;

public class ObjectMapperNameDeserializer extends JsonDeserializer<NameBean> {

    private final String mapperName;

    public ObjectMapperNameDeserializer(String mapperName) {

        this.mapperName = mapperName;

    }

    @Override
    public NameBean deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {

        String s = jsonParser.getValueAsString();

        if (s.equals("value")) {

            return new NameBean(mapperName);
        }

        return new NameBean(s);
    }

}
