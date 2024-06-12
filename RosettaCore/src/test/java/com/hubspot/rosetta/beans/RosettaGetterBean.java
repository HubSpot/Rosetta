package com.hubspot.rosetta.beans;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hubspot.rosetta.annotations.RosettaSerializationProperty;

public class RosettaGetterBean {

  @RosettaSerializationProperty("mccartney_song_title")
  private String mcCartneySongTitle;

  @RosettaSerializationProperty
  @JsonIgnore
  private String jsonIgnoreRosettaUse;

  @JsonProperty("otherValue")
  @RosettaSerializationProperty("other_value")
  private String someOtherValue;

  public String getMcCartneySongTitle() {
    return mcCartneySongTitle;
  }

  public RosettaGetterBean setMcCartneySongTitle(String mcCartneySongTitle) {
    this.mcCartneySongTitle = mcCartneySongTitle;
    return this;
  }

  public String getJsonIgnoreRosettaUse() {
    return jsonIgnoreRosettaUse;
  }

  public String getSomeOtherValue() {
    return someOtherValue;
  }

  public RosettaGetterBean setSomeOtherValue(String someOtherValue) {
    this.someOtherValue = someOtherValue;
    return this;
  }

  public RosettaGetterBean setJsonIgnoreRosettaUse(String jsonIgnoreRosettaUse) {
    this.jsonIgnoreRosettaUse = jsonIgnoreRosettaUse;
    return this;
  }
}
