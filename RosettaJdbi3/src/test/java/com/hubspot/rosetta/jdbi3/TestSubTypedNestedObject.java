package com.hubspot.rosetta.jdbi3;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = As.EXISTING_PROPERTY,
    property = "color",
    visible = true
)
@JsonSubTypes(
    {
        @Type(value = TestGreenNestedObject.class, name = "GREEN"),
        @Type(value = TestRedNestedObject.class, name = "RED"),
    }
)
public interface TestSubTypedNestedObject {
  int getRelatedId();
  String getColor();
}
