package com.hubspot.rosetta;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.base.Optional;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.hubspot.rosetta.jackson.RosettaView;

/**
 * The compliment to {@link RosettaMapper}, it binds object fields to named parameters.
 *
 * @author tdavis
 */
public class RosettaBinder {

  private final Tablet tablet;
  private final Map<String, PropertyDefinition> availableFields;

  public RosettaBinder(Class<?> type) {
    this.tablet = Rosetta.tabletForType(type);
    this.availableFields = tablet.getFields(type, false);

    tablet.useView(RosettaView.class);
  }

  /**
   * Construct a generic mapping of parameter names to objects.
   */
  public Map<String, Object> makeBoundMap(final Object object) {
    return makeBoundMap(object, false);
  }


  /**
   * Construct a generic mapping of parameter names to objects.
   */
  public Map<String, Object> makeBoundMap(final Object object, boolean includeNulls) {
    final Map<String, Object> propertyMap = new HashMap<String, Object>();
    PropertyDefinition parent = null;
    Object realObject;
    Object value;
    Optional<Object> customValue;
    PropertyDefinition definition;

    for (final Map.Entry<String, PropertyDefinition> entry : availableFields.entrySet()) {
      customValue = Optional.absent();
      realObject = object;
      definition = entry.getValue();

      if (!definition.couldBind()) {
        continue;
      }

      final List<String> layers = Lists.newArrayList(Splitter.on(".").split(entry.getKey()));

      if (definition.hasParent()) {  // We have a child field; need to inspect the right object
        parent = definition.getParent();
        realObject = parent.getValue(object);
        Tablet childTablet = parent.getTablet();
        definition = childTablet.getFields(parent.getType(), false).get(layers.get(1));
      }
      value = definition.getValue(realObject);
      if (value != null) {
        customValue = maybeCustomBoundValue(value);
        propertyMap.put(entry.getKey(), customValue.or(value));
      } else if (includeNulls) {
        propertyMap.put(entry.getKey(), null);
      }
    }
    return propertyMap;
  }

  private Optional<Object> maybeCustomBoundValue(Object fieldInstance) {
    if (CustomBoundValue.class.isAssignableFrom(fieldInstance.getClass())) {
      Object val = ((CustomBoundValue)fieldInstance).getBoundValue();
      return Optional.of(val);
    }
    return Optional.absent();
  }
}
