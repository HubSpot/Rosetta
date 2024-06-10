package com.hubspot.rosetta.beans;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hubspot.rosetta.annotations.RosettaGetter;

public class RosettaGetterBean {

  @RosettaGetter("mccartney_song_title")
  private String mcCartneySongTitle;

  @RosettaGetter
  @JsonIgnore
  private String jsonIgnoreRosettaUse;

  @JsonProperty("otherValue")
  @RosettaGetter("other_value")
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
