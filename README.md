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
  <version>3.10.2</version>
</dependency>
```

## Binding

You can [bind JDBI arguments](http://www.jdbi.org/sql_object_api_argument_binding) in your DAO using `@BindWithRosetta`.

```java
public interface MyDAO {
  @SqlUpdate("UPDATE my_table SET field_one = :field_one, field_two = :field_two WHERE id = :id")
  void update(@BindWithRosetta MyRow obj);
}
```

`@BindWithRosetta` behaves like jDBI's `@BindBean`, but it converts the object to a tree using Jackson which lets
you use all the Jackson annotations you know and love to customize the representation. It's also generally more robust - it supports the not-quite-standard naming conventions, enums, fluent setters, nested objects (with dot-notation), getters without fields, etc.

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

Or to use in combination with a `Handler`:
```java
Query<Map<String, Object>> query = handle.createQuery("SELECT * FROM my_table");
query.registerMapper(new RosettaMapperFactory());
query.mapTo(MyRow.class).list();
```

## Configuration

Assuming your Java field names are camel-case and your SQL column names are lowercase with underscores (a pretty common scenario) you can make this work with Rosetta by annotating the Java objects with `@RosettaNaming(LowerCaseWithUnderscoresStrategy.class)` which will change Jackson's naming strategy to lower case with underscores but only for Rosetta binding/mapping (other Jackson operations throughout your application are unaffected). 

Or to make this change globally for Rosetta, you could make a Jackson `Module` that sets the default naming strategy to lowercase with underscores, something like this:

```java
public class LowerCaseWithUnderscoresModule extends SimpleModule {

  @Override
  public void setupModule(SetupContext context) {
    context.setNamingStrategy(new LowerCaseWithUnderscoresStrategy());
  }
}
```

You can then register it with Rosetta programmatically during your app's startup with something like:

```java
Rosetta.addModule(new LowerCaseWithUnderscoresModule());
```

Or even better, make your module extend `com.hubspot.rosetta.databind.AutoDiscoveredModule` and create a file located at
`src/main/resources/META-INF/services/com.hubspot.rosetta.databind.AutoDiscoveredModule` containing a newline separated list of fully qualified class names for the modules you want to load, in this case it would contain something like `your.package.LowerCaseWithUnderscoresModule`.

(You can also do this for the default Module service loader location: `src/main/resources/META-INF/services/com.fasterxml.jackson.databind.Module`)
