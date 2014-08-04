# RosettaJdbi

This module provides implementations of common JDBI mapper/binder interfaces using Rosetta.

Tired of going from `@BindBean` to `@BindBeanWithEnums` to `@BindMyStupidClass`? Hate implementing a separate `ResultSetMapper` for every minor edge case that `BeanMapper` doesn't cover? Don't know what half that shit means? Then `RosettaJdbi` is for you.

## Installation

    <dependency>
        <groupId>com.hubspot.rosetta</groupId>
        <artifactId>RosettaJdbi</artifactId>
        <version>2.1-SNAPSHOT</version>
    </dependency>

## Mapping


Yu can either register the Rosetta mapper on your DAO:

```java
@RegisterMapperFactory(RosettaResultSetMapperFactory.class)
public interface MyDAO { /* ... */ }
```

Or use it to map `Handler` results:

```java
handle.createQuery("select * from myTable")
  .map(RosettaResultSetMapperFactory.<MyRow>mapperFor(MyRow.class))
  .list();
```

### Extras

Bonus mapping features included in your order:

* Automatic "snake_case" handling by annotating a class with `SnakeCase`.


## Binding

You can [bind JDBI arguments](http://www.jdbi.org/sql_object_api_argument_binding) in your DAO using `@RosettaBinder`.
```java
public interface MyDAO {
  @SqlUpdate("UPDATE my_table "
            +"SET some_field=:some_file, another_field=:another_field "
            +"WHERE id=:id")
  void update(@RosettaBinder MyRow obj);
}
```

This example assumes that `MyRow` contains properties `some_field`, `another_field` and `id` (most likely camel-cased in the Java class but annotated with `@SnakeCase`).

`@RosettaBinder` behaves like JDBI's `@BindBean`, but it lets you customize mapped field names using Jackson annotations. It's also generally more robust - it supports the not-quite-standard naming conventions, fluent setters, getters without fields, etc.
