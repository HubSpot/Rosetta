package com.hubspot.rosetta;

/**
 * Implement this interface to customize how a type's representation is bound.
 *
 */
public interface CustomBoundValue {

  Object getBoundValue();

}
