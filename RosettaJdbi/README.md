# RosettaJdbi

This module provides thin adapters to make RosettaBinder and RosettaMapper easily pluggable as the mapping/binding implementation for users of JDBI.

Tired of going from `@BindBean` to `@BindBeanWithEnums` to `@BindMyStupidClass`? Hate implementing a separate `ResultSetMapper` for every minor edge case that `BeanMapper` doesn't cover? Don't know what half that shit means? Then `RosettaJdbi` is for you.

### Maven dependency

To use module on Maven-based projects, use following dependency:

```xml
<dependency>
  <groupId>com.hubspot.rosetta</groupId>
  <artifactId>RosettaJdbi</artifactId>
  <version>3.10.0</version>
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
