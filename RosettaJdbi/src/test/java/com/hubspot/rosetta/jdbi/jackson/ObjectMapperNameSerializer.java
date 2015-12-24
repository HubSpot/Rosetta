package com.hubspot.rosetta.jdbi.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.hubspot.rosetta.jdbi.beans.NameBean;

import java.io.IOException;

public class ObjectMapperNameSerializer extends JsonSerializer<NameBean> {

    private final String mapperName;

    public ObjectMapperNameSerializer(String mapperName) {

        this.mapperName = mapperName;
    }

    @Override
    public void serialize(NameBean nameBean, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {

        String s = nameBean.getName();

        if (s.equals("value")) {

            jsonGenerator.writeString(mapperName);
        }

        else {

            jsonGenerator.writeString(s);
        }
    }
}
