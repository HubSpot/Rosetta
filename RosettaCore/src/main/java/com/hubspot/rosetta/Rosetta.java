package com.hubspot.rosetta;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hubspot.rosetta.databind.AutoDiscoveredModule;
import com.hubspot.rosetta.internal.RosettaModule;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicReference;
import java.util.Collection;

/**
 * Static public APIs to get/set some Rosetta globals.
 */
public enum Rosetta {
  INSTANCE;

  private static final List<Module> MODULES = new CopyOnWriteArrayList<Module>(defaultModules());
  private static final AtomicReference<ObjectMapper> MAPPER = new AtomicReference<ObjectMapper>(cloneAndCustomize(new ObjectMapper()));
  private static final Map<String, ObjectMapper> NAMED_MAPPERS = new ConcurrentHashMap<String, ObjectMapper>();

  public static ObjectMapper getMapper() {
    return INSTANCE.get();
  }

  public static void addModule(Module module) {
    INSTANCE.add(module);
  }

  public static void setMapper(ObjectMapper mapper) {
    INSTANCE.set(mapper);
  }

  public static ObjectMapper cloneAndCustomize(ObjectMapper mapper) {
    mapper = mapper.copy();

    // ObjectMapper#registerModules doesn't exist in 2.1.x
    for (Module module : MODULES) {
      mapper.registerModule(module);
    }

    return mapper;
  }

  private ObjectMapper get() {
    return MAPPER.get();
  }

  private void set(ObjectMapper mapper) {
    MAPPER.set(cloneAndCustomize(mapper));
  }

  private void add(Module module) {
    MODULES.add(module);
    MAPPER.get().registerModule(module);

    for (ObjectMapper objectMapper : NAMED_MAPPERS.values()) {
      objectMapper.registerModule(module);
    }
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

  public static void registerNamedMapper(String name, ObjectMapper objectMapper) {

    NAMED_MAPPERS.put(name, cloneAndCustomize(objectMapper));
  }

  public static void registerNamedMappers(Map<String, ObjectMapper> objectMappers) {

    for (Map.Entry<String, ObjectMapper> entry : objectMappers.entrySet()) {

      NAMED_MAPPERS.put(entry.getKey(), cloneAndCustomize(entry.getValue()));
    }
  }

  public static ObjectMapper removeNamedMapper(String name) {

    return NAMED_MAPPERS.remove(name);
  }

  public static Map<String, ObjectMapper> removeNamedMappers(Collection<String> names) {

    Map<String, ObjectMapper> removedMappers = new ConcurrentHashMap<>();

    for (String name : names) {

      ObjectMapper objectMapper = removeNamedMapper(name);

      if (objectMapper != null) {

        removedMappers.put(name, removeNamedMapper(name));

      }

    }

    return removedMappers;
  }

  public static Map<String, ObjectMapper> getNamedMappers() {

    return new ConcurrentHashMap<>(NAMED_MAPPERS);
  }

  public static ObjectMapper getNamedMapper(String mapperName) {

    return NAMED_MAPPERS.get(mapperName);
  }

  public static void clearNamedMappers() {

    NAMED_MAPPERS.clear();
  }

}
