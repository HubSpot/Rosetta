package com.hubspot.rosetta.tablets;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;

import com.google.common.primitives.Primitives;
import com.hubspot.rosetta.Tablet;
import com.hubspot.rosetta.TabletFactory;

public class BeanTabletFactory implements TabletFactory {

  public BeanTabletFactory() {}

  @Override
  public boolean accepts(Class<?> type) {
    if (type.isArray() || Primitives.isWrapperType(type) || type.isAnnotation() || String.class.isAssignableFrom(type)) {
      return false;
    }
    final BeanInfo info = getBeanInfo(type);
    if (info == null) {
      return false;
    }
    final PropertyDescriptor[] props = info.getPropertyDescriptors();
    return props.length > 0;
  }

  private BeanInfo getBeanInfo(Class<?> type) {
    BeanInfo info = null;
    try {
      info = Introspector.getBeanInfo(type);
    } catch (IntrospectionException e) {}

    return info;
  }

  @Override
  public Tablet getInstance(Class<?> type) {
    return new BeanTablet();
  }

}
