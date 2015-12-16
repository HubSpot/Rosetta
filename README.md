# Rosetta [![Build Status](https://travis-ci.org/HubSpot/Rosetta.svg?branch=master)](https://travis-ci.org/HubSpot/Rosetta)

## Overview

Rosetta is a Java library that leverages [Jackson](https://github.com/FasterXML/jackson) to take the pain out of mapping objects to/from the DB, designed to integrate seamlessly with [jDBI](https://github.com/jdbi/jdbi). Jackson is extremely fast, endlessly configurable, and already used by many Java webapps. 

Rosetta isn't an ORM. It doesn't silently read and write to your database, validate input, or manage connections. It does two things:

1. Binds Java objects to query parameters using a jDBI `BindingAnnotation`
2. Maps query results back to Java objects using a jDBI `ResultSetMapperFactory`

## Usage

To use with jDBI on Maven-based projects, add the following dependency:

```xml
<dependency>
  <groupId>com.hubspot.rosetta</groupId>
  <artifactId>RosettaJdbi</artifactId>
  <version>3.10.4</version>
</dependency>
```

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
