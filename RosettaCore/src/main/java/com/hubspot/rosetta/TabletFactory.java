package com.hubspot.rosetta;

public interface TabletFactory {
  boolean accepts(Class<?> type);
  Tablet getInstance(Class<?> type);
}
