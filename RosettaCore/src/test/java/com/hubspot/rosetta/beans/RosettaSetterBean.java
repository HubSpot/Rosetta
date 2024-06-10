package com.hubspot.rosetta.beans;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hubspot.rosetta.annotations.RosettaSetter;

public class RosettaSetterBean {

  @RosettaSetter("mccartney_song_title")
  private String mcCartneySongTitle;

  @RosettaSetter
  @JsonIgnore
  private String jsonIgnoreRosettaUse;

  @JsonProperty("otherValue")
  @RosettaSetter("other_value")
  private String someOtherValue;

  public String getMcCartneySongTitle() {
    return mcCartneySongTitle;
  }

  public RosettaSetterBean setMcCartneySongTitle(String mcCartneySongTitle) {
    this.mcCartneySongTitle = mcCartneySongTitle;
    return this;
  }

  public String getSomeOtherValue() {
    return someOtherValue;
  }

  public RosettaSetterBean setSomeOtherValue(String someOtherValue) {
    this.someOtherValue = someOtherValue;
    return this;
  }

  public String getJsonIgnoreRosettaUse() {
    return jsonIgnoreRosettaUse;
  }

  public RosettaSetterBean setJsonIgnoreRosettaUse(String jsonIgnoreRosettaUse) {
    this.jsonIgnoreRosettaUse = jsonIgnoreRosettaUse;
    return this;
  }
}
