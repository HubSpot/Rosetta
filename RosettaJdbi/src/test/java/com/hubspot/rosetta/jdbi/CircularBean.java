package com.hubspot.rosetta.jdbi;

import java.util.List;

public class CircularBean {

  public CircularBean getNested() {
    return null;
  }

  public void setNested(CircularBean bean) {}

  public List<CircularBean> getNestedList() {
    return null;
  }

  public void setNestedList(List<CircularBean> beans) {}
}
