package com.hubspot.rosetta;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hubspot.rosetta.databind.AutoDiscoveredModule;
import com.hubspot.rosetta.internal.RosettaModule;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Static public APIs to get/set some Rosetta globals.
 */
public enum Rosetta {
  INSTANCE;

  private final List<Module> modules = new CopyOnWriteArrayList<Module>(defaultModules());
  private final AtomicReference<ObjectMapper> mapper = new AtomicReference<ObjectMapper>(cloneAndCustomize(new ObjectMapper()));

  public static ObjectMapper getMapper() {
    return INSTANCE.get();
  }

  public static void addModule(Module module) {
    INSTANCE.add(module);
  }

  public static void setMapper(ObjectMapper mapper) {
    INSTANCE.set(mapper);
  }

  private ObjectMapper get() {
    return mapper.get();
  }

  private void set(ObjectMapper mapper) {
    this.mapper.set(cloneAndCustomize(mapper));
  }

  private void add(Module module) {
    modules.add(module);
    mapper.get().registerModule(module);
  }

  private ObjectMapper cloneAndCustomize(ObjectMapper mapper) {
    mapper = mapper.copy();

    // ObjectMapper#registerModules doesn't exist in 2.1.x
    for (Module module : modules) {
      mapper.registerModule(module);
    }

    return mapper;
  }

  private static List<Module> defaultModules() {
    List<Module> defaultModules = new ArrayList<Module>();
    defaultModules.add(new RosettaModule());

    for (Module module : ServiceLoader.load(AutoDiscoveredModule.class)) {
      defaultModules.add(module);
    }

    for (Module module : ServiceLoader.load(Module.class)) {
      defaultModules.add(module);
    }

    return defaultModules;
  }
}
