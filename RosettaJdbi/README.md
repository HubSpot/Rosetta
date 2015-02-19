# RosettaJdbi

This module provides thin adapters to make RosettaBinder and RosettaMapper easily pluggable as the mapping/binding implementation for users of JDBI.

Tired of going from `@BindBean` to `@BindBeanWithEnums` to `@BindMyStupidClass`? Hate implementing a separate `ResultSetMapper` for every minor edge case that `BeanMapper` doesn't cover? Don't know what half that shit means? Then `RosettaJdbi` is for you.

### Maven dependency

To use module on Maven-based projects, use following dependency:

```xml
<dependency>
  <groupId>com.hubspot.rosetta</groupId>
  <artifactId>RosettaJdbi</artifactId>
  <version>3.4</version>
</dependency>
```

(or whatever version is most up-to-date at the moment)

## Mapping

You can either register the Rosetta mapper globally with your DBI instance:

```java
dbi.registerMapper(new RosettaMapperFactory());
```

Or on your DAO:

```java
@RegisterMapperFactory(RosettaMapperFactory.class)
public interface MyDAO { /* ... */ }
```

Or use it to map `Handler` results:

```java
handle.createQuery("select * from myTable")
  .map(new RosettaMapper<MyRow>(MyRow.class))
  .list();
```


## Binding

You can [bind JDBI arguments](http://www.jdbi.org/sql_object_api_argument_binding) in your DAO using `@RosettaBinder`.
```java
public interface MyDAO {
  @SqlUpdate("UPDATE my_table "
            +"SET some_field=:some_file, another_field=:another_field "
            +"WHERE id=:id")
  void update(@BindWithRosetta MyRow obj);
}
```

This example assumes that `MyRow` contains properties `some_field`, `another_field` and `id` (most likely camel-cased in the Java class but annotated with `@RosettaNaming(LowerCaseWithUnderscoresStrategy.class)`).

`@BindWithRosetta` behaves like JDBI's `@BindBean`, but it lets you customize mapped field names using Jackson annotations. It's also generally more robust - it supports the not-quite-standard naming conventions, fluent setters, nested objects (with dot-notation), getters without fields, etc.
