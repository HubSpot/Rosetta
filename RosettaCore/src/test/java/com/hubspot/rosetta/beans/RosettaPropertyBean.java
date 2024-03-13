package com.hubspot.rosetta.beans;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hubspot.rosetta.annotations.RosettaProperty;

public class RosettaPropertyBean {

  @RosettaProperty("mccartney_song_title")
  private String mcCartneySongTitle;

  @RosettaProperty
  @JsonIgnore
  private String jsonIgnoreRosettaUse;

  public String getMcCartneySongTitle() {
    return mcCartneySongTitle;
  }

  public RosettaPropertyBean setMcCartneySongTitle(String mcCartneySongTitle) {
    this.mcCartneySongTitle = mcCartneySongTitle;
    return this;
  }

  public String getJsonIgnoreRosettaUse() {
    return jsonIgnoreRosettaUse;
  }

  public RosettaPropertyBean setJsonIgnoreRosettaUse(String jsonIgnoreRosettaUse) {
    this.jsonIgnoreRosettaUse = jsonIgnoreRosettaUse;
    return this;
  }
}
