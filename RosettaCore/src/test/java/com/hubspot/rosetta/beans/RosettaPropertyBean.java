package com.hubspot.rosetta.beans;

import com.hubspot.rosetta.annotations.RosettaProperty;

public class RosettaPropertyBean {
  @RosettaProperty("hubspot_customer_name")
  private String hubSpotCustomerName;

  public String getHubSpotCustomerName() {
    return hubSpotCustomerName;
  }

  public RosettaPropertyBean setHubSpotCustomerName(String hubSpotCustomerName) {
    this.hubSpotCustomerName = hubSpotCustomerName;
    return this;
  }
}
