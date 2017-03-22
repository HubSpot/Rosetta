package com.hubspot.rosetta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ServiceLoader;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicReference;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hubspot.rosetta.databind.AutoDiscoveredModule;
import com.hubspot.rosetta.internal.RosettaModule;

/**
 * Static public APIs to get/set some Rosetta globals.
 */
public enum Rosetta {
  INSTANCE;

  private static final List<Module> DISCOVERED_MODULES = Collections.unmodifiableList(discoverModules());
  private static final List<Module> CUSTOM_MODULES = new CopyOnWriteArrayList<>();
  private static final AtomicReference<ObjectMapper> MAPPER = new AtomicReference<>(cloneAndCustomize(new ObjectMapper(), true));

  public static ObjectMapper getMapper() {
    return INSTANCE.get();
  }

  public static void addModule(Module module) {
    INSTANCE.add(module);
  }

  public static void setMapper(ObjectMapper mapper) {
    setMapper(mapper, true);
  }

  public static void setMapper(ObjectMapper mapper, boolean registerDiscoveredModules) {
    INSTANCE.set(mapper, registerDiscoveredModules);
  }

  public static ObjectMapper cloneAndCustomize(ObjectMapper mapper) {
    return cloneAndCustomize(mapper, true);
  }

  public static ObjectMapper cloneAndCustomize(ObjectMapper mapper, boolean registerDiscoveredModules) {
    mapper = mapper.copy();

    mapper.registerModule(new RosettaModule());

    if (registerDiscoveredModules) {
      // ObjectMapper#registerModules doesn't exist in 2.1.x
      for (Module module : DISCOVERED_MODULES) {
        mapper.registerModule(module);
      }
    }

    // ObjectMapper#registerModules doesn't exist in 2.1.x
    for (Module module : CUSTOM_MODULES) {
      mapper.registerModule(module);
    }

    return mapper;
  }

  private ObjectMapper get() {
    return MAPPER.get();
  }

  private void add(Module module) {
    CUSTOM_MODULES.add(module);
    MAPPER.get().registerModule(module);
  }

  private void set(ObjectMapper mapper, boolean registerDiscoveredModules) {
    MAPPER.set(cloneAndCustomize(mapper, registerDiscoveredModules));
  }

  private static List<Module> discoverModules() {
    List<Module> defaultModules = new ArrayList<>();

    for (Module module : ServiceLoader.load(AutoDiscoveredModule.class)) {
      defaultModules.add(module);
    }

    for (Module module : ServiceLoader.load(Module.class)) {
      defaultModules.add(module);
    }

    return defaultModules;
  }
}
