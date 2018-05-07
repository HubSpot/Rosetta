# Rosetta [![Build Status](https://travis-ci.org/HubSpot/Rosetta.svg?branch=master)](https://travis-ci.org/HubSpot/Rosetta)

## Overview

Rosetta is a Java library that leverages [Jackson](https://github.com/FasterXML/jackson) to take the pain out of mapping objects to/from the DB, designed to integrate seamlessly with [Jdbi](https://github.com/jdbi/jdbi). Jackson is extremely fast, endlessly configurable, and already used by many Java webapps.

## Usage

If you are on Jdbi 2, add the following dependency:

```xml
<dependency>
  <groupId>com.hubspot.rosetta</groupId>
  <artifactId>RosettaJdbi</artifactId>
  <version>{latest version}</version>
</dependency>
```

Latest versions can be seen [here](https://search.maven.org/#search%7Cga%7C1%7Cg%3A%22com.hubspot.rosetta%22)

Or if you are on Jdbi 3, add the following dependency:

```xml
<dependency>
  <groupId>com.hubspot.rosetta</groupId>
  <artifactId>RosettaJdbi3</artifactId>
  <version>{latest version}</version>
</dependency>
```

Latest versions can be seen [here](https://search.maven.org/#search%7Cga%7C1%7Cg%3A%22com.hubspot.rosetta%22)

## Binding

You can [bind JDBI arguments](http://jdbi.org/#_binding_arguments) in your DAO using `@BindWithRosetta`.

```java
public interface MyDAO {
  @SqlUpdate("UPDATE my_table SET name = :name, type = :type WHERE id = :id")
  void update(@BindWithRosetta MyRow obj);
}
```

`@BindWithRosetta` converts the object to a tree using Jackson, and then binds every property in the JSON tree on the Jdbi statement (using dot notation for nested object fields). This lets you use all the Jackson annotations you know and love to customize the representation.

## Mapping

### Jdbi 2

With Jdbi 2, you can register the Rosetta mapper globally by adding it your `DBI` like so:
```java
dbi.registerMapper(new RosettaMapperFactory());
```

Or to test it out on a single DAO you would do:
```java
@RegisterMapperFactory(RosettaMapperFactory.class)
public interface MyDAO { /* ... */ }
```

Or to use in combination with a `Handle`: (same idea to register on a `Query`)
```java
handle.registerMapper(new RosettaMapperFactory());
```

### Jdbi 3

With Jdbi 3, you can register the Rosetta mapper globally by adding it your `Jdbi` like so:
```java
jdbi.registerRowMapper(new RosettaRowMapperFactory());
```

Or to test it out on a single DAO you would do:
```java
@RegisterRowMapperFactory(RosettaRowMapperFactory.class)
public interface MyDAO { /* ... */ }
```

Or to use in combination with a `Handle`: (same idea to register on a `Query`)
```java
handle.registerRowMapper(new RosettaRowMapperFactory());
```

## Advanced Features

For a list of advanced features, see [here](FEATURES.md)
