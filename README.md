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

You can [bind JDBI arguments](http://www.jdbi.org/sql_object_api_argument_binding) in your DAO using `@BindWithRosetta`.

```java
public interface MyDAO {
  @SqlUpdate("UPDATE my_table SET name = :name, type = :type WHERE id = :id")
  void update(@BindWithRosetta MyRow obj);
}
```

`@BindWithRosetta` behaves like jDBI's `@BindBean`, but it converts the object to a tree using Jackson which lets
you use all the Jackson annotations you know and love to customize the representation. It's also generally more robust - it supports
the not-quite-standard naming conventions, enums, fluent setters, nested objects (with dot-notation), getters without fields, etc.

## Mapping

To register Rosetta globally for mapping, you can add it to your `DBI` like so:
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

## Advanced Features

For a list of advanced features, see [here](FEATURES.md)
