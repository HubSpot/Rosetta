package com.hubspot.rosetta.jdbi.jackson;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.hubspot.rosetta.jdbi.beans.NameBean;

public class ObjectMapperResolutionTestModule extends SimpleModule {

    public ObjectMapperResolutionTestModule(final String name) {

        addDeserializer(NameBean.class, new ObjectMapperNameDeserializer(name));
        addSerializer(NameBean.class, new ObjectMapperNameSerializer(name));
        setNamingStrategy(new PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy());
    }


}
