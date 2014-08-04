package com.hubspot.rosetta;

/**
 * Implement this interface to customize how a type's representation is mapped.
 *
 */
public interface CustomMappedValue {

  Object getMappedValue(Object defaultValue);

}
