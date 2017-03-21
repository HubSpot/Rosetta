package com.hubspot.rosetta;

import java.util.ArrayList;
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

  private static final List<Module> CUSTOM_MODULES = new CopyOnWriteArrayList<Module>();
  private static final AtomicReference<ObjectMapper> MAPPER = new AtomicReference<ObjectMapper>(cloneAndCustomize(new ObjectMapper(), loadAvailableModules()));

  public static ObjectMapper getMapper() {
    return INSTANCE.get();
  }

  public static void addModule(Module module) {
    INSTANCE.add(module);
  }

  public static void setMapper(ObjectMapper mapper) {
    INSTANCE.set(mapper);
  }

  public static ObjectMapper cloneAndCustomize(ObjectMapper mapper, List<Module> modules) {
    mapper = mapper.copy();

    mapper.registerModule(new RosettaModule());

    // ObjectMapper#registerModules doesn't exist in 2.1.x
    for (Module module : modules) {
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

  private void set(ObjectMapper mapper) {
    MAPPER.set(cloneAndCustomize(mapper, CUSTOM_MODULES));
  }

  private static List<Module> loadAvailableModules() {
    List<Module> defaultModules = new ArrayList<Module>();

    for (Module module : ServiceLoader.load(AutoDiscoveredModule.class)) {
      defaultModules.add(module);
    }

    for (Module module : ServiceLoader.load(Module.class)) {
      defaultModules.add(module);
    }

    return defaultModules;
  }
}
