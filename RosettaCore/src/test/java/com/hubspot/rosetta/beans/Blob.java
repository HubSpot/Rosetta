package com.hubspot.rosetta.beans;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.hubspot.rosetta.annotations.StoredAsJson;

public class Blob {

  String name;
  @StoredAsJson
  Person blob;
  @StoredAsJson(empty = "[]")
  ArrayNode anArray;

  public Blob() {}

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Person getBlob() {
    return blob;
  }

  public void setBlob(Person blob) {
    this.blob = blob;
  }

  public ArrayNode getAnArray() {
    return anArray;
  }

  public void setAnArray(ArrayNode anArray) {
    this.anArray = anArray;
  }

}
