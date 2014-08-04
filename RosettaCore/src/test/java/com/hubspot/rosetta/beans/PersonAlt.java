package com.hubspot.rosetta.beans;

import com.hubspot.rosetta.ColumnName;

public class PersonAlt {

  private String name;
  private int age;
  private GenderPojo gender;

  @ColumnName("nom")
  public String getName() {
    return name;
  }

  @ColumnName("nom")
  public void setName(String name) {
    this.name = name;
  }

  public int getAge() {
    return age;
  }

  public void setAge(int age) {
    this.age = age;
  }

  public GenderPojo getGender() {
    return gender;
  }

  public void setGender(GenderPojo gender) {
    this.gender = gender;
  }

}
