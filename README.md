# Rosetta [![Build Status](https://travis-ci.org/HubSpot/Rosetta.svg?branch=master)](https://travis-ci.org/HubSpot/Rosetta)

## Overview

Rosetta is a Java library that leverages [Jackson](https://github.com/FasterXML/jackson) to take the pain out of mapping objects to/from the DB, designed to integrate seamlessly with [jDBI](https://github.com/jdbi/jdbi). Jackson is extremely fast, endlessly configurable, and already used by many Java webapps. 

Rosetta isn't an ORM. It doesn't silently read and write to your database, validate input, or manage connections. It does two things:

1. Binds Java objects to SQL query parameters
2. Maps SQL result sets to Java objects

## Usage

To use with jDBI on Maven-based projects, add the following dependency:

```xml
<dependency>
  <groupId>com.hubspot.rosetta</groupId>
  <artifactId>RosettaJdbi</artifactId>
  <version>3.4</version>
</dependency>
```

## Binding

You can [bind JDBI arguments](http://www.jdbi.org/sql_object_api_argument_binding) in your DAO using `@BindWithRosetta`.

```java
public interface MyDAO {
  @SqlUpdate("UPDATE my_table "
            +"SET some_field=:someField, another_field=:anotherField "
            +"WHERE id=:id")
  void update(@BindWithRosetta MyRow obj);
}
```

`@BindWithRosetta` behaves like jDBI's `@BindBean`, but it converts the object to a tree using Jackson which lets
you use all the Jackson annotations you know and love to customize the representation. It's also generally more robust - it supports the not-quite-standard naming conventions, fluent setters, nested objects (with dot-notation), getters without fields, etc.

## Mapping

To register Rosetta globally for mapping you can add it to your `DBI` like so:
```java
dbi.registerMapper(new RosettaMapperFactory());
```

Or to test it out on a single DAO you would do:
```java
@RegisterMapperFactory(RosettaMapperFactory.class)
public interface MyDAO { /* ... */ }
```
