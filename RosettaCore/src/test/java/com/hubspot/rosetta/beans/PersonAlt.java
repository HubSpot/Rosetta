package com.hubspot.rosetta.beans;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PersonAlt {

  @JsonProperty("nom")
  private String name;
  private int age;
  private GenderPojo gender;

  public String getName() {
    return name;
  }

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
