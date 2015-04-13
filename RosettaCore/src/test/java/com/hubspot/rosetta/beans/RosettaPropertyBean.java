package com.hubspot.rosetta.beans;

import com.hubspot.rosetta.annotations.RosettaProperty;

public class RosettaPropertyBean {
  @RosettaProperty("mccartney_song_title")
  private String mcCartneySongTitle;

  public String getMcCartneySongTitle() {
    return mcCartneySongTitle;
  }

  public RosettaPropertyBean setMcCartneySongTitle(String mcCartneySongTitle) {
    this.mcCartneySongTitle = mcCartneySongTitle;
    return this;
  }
}
