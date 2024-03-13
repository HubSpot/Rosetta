package com.hubspot.rosetta.beans;

import com.google.common.base.MoreObjects;
import com.hubspot.rosetta.annotations.StoredAsJson;
import java.util.Objects;

public class NestedStoredAsJsonBean {

  @StoredAsJson
  private StoredAsJsonBean annotatedField;

  @StoredAsJson
  public StoredAsJsonBean getAnnotatedField() {
    return annotatedField;
  }

  @StoredAsJson
  public void setAnnotatedField(StoredAsJsonBean annotatedField) {
    this.annotatedField = annotatedField;
  }

  @Override
  public int hashCode() {
    return Objects.hash(annotatedField);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    } else if (obj == null || !obj.getClass().equals(getClass())) {
      return false;
    }

    NestedStoredAsJsonBean that = (NestedStoredAsJsonBean) obj;
    return Objects.equals(that.annotatedField, this.annotatedField);
  }

  @Override
  public String toString() {
    return MoreObjects
      .toStringHelper(this)
      .add("annotatedField", annotatedField)
      .toString();
  }
}
