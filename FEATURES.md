
# Rosetta Advanced Features

* [How does Rosetta work?](#how-does-rosetta-work)
* [Nested Objects](#nested-objects)
  * [Binding](#binding)
  * [Mapping](#mapping)
* [Fields Stored as JSON](#fields-stored-as-json)
* [Custom Naming Strategies](#custom-naming-strategies)
* [Providing your own ObjectMapper](#providing-your-own-objectmapper)
* [Working with hubspot-immutables](#working-with-hubspot-immutables)

## How does Rosetta work?

Rosetta is mostly a thin layer of top of Jackson, which it uses to do all the heavy lifting. When binding a parameter, Rosetta converts
the object to a JSON tree (specifically, it uses Jackson's `JsonNode`). Then it iterates over this tree and binds each JSON property
(nested object properties are bound using dot-notation). 

When mapping a parameter, Rosetta builds up a map that represents all of the object values. It does this by iterating over the columns in
the `ResultSet` and using the `ResultSetMetaData` to introspect the column label. Each value is added to the map with the column label
as the key. Once the map is built, we ask Jackson to convert the map into the desired result type.

## Nested Objects

Rosetta supports binding and mapping of arbitrarily nested object graphs. Dot-notation is used to separate fields of subobjects. The 
following examples will use these object definitions:

```java
public class OuterBean {
  private String type;
  private InnerBean inner;

  // getters and setters
}

public class InnerBean {
  private long id;
  private String name;
  
  // getters and setters
}
```

### Binding

If binding an instance of `OuterBean` with Rosetta, the inner object fields will be bound and prefixed with `inner.`, an example query
could look like:

```java
@SqlUpdate("INSERT INTO my_table (type, id, name) VALUES (:type, :inner.id, :inner.name)")
public void insert(@BindWithRosetta OuterBean bean);
```

### Mapping

On the deserialization side, things are slightly trickier. At a conceptual level, for mapping to work the JSON we pass to Jackson
needs to have the following structure, since this is the expected structure based on our object graph:
```json
{
  "type": "car",
  "inner": {
    "id": 123,
    "name": "derp"
  }
}
```

When mapping a `ResultSet`, Rosetta creates two entries for each column. One for the column name (or alias), and one for the column
name prefixed with the table name. So in our previous example, if we wrote a method such as:
```java
@SqlQuery("SELECT type, id, name FROM my_table WHERE id = :id")
public OuterBean retrieve(@Bind("id") long id);
```

We would end up with a JSON structure that looked like:
```json
{
  "type": "car",
  "id": 123,
  "name": "derp",
  "my_table": {
    "type": "car",
    "id": 123,
    "name": "derp"
  }
}
```

Which won't deserialize correctly because it doesn't match our object structure. To make this work we have two options. The first is
to alias the column names in the select statement, which would look like this:
```java
@SqlQuery("SELECT type, id AS `inner.id`, name AS `inner.name` FROM my_table WHERE id = :id")
public OuterBean retrieve(@Bind("id") long id);
```

Which would generate the following JSON structure:
```json
{
  "type": "car",
  "inner": {
    "id": 123,
    "name": "derp"
  },
  "my_table": {
    "type": "car",
    "id": 123,
    "name": "derp"
  }
}
```
Which would deserialize correctly (the unknown `my_table` field would be ignored by Jackson because we configure the `ObjectMapper` to
ignore unknown fields).

The other option is to alias the table rather than the column, which would look like this:
```java
@SqlQuery("SELECT type, id, name FROM my_table AS inner WHERE id = :id")
public OuterBean retrieve(@Bind("id") long id);
```

Which would generate the following JSON structure:
```json
{
  "type": "car",
  "id": 123,
  "name": "derp",
  "inner": {
    "type": "car",
    "id": 123,
    "name": "derp"
  }
}
```

Which would also deserialize correctly. So if you're trying to map a complex object graph, remember how Rosetta generates the JSON
structure and you should be able to use column and/or table aliasing to make it work.

## Fields Stored as JSON

Let's say we modify the objects from the previous example so that `OuterBean` now contains a list of `InnerBean` 
(`InnerBean` definition is unchanged):

```java
public class OuterBean {
  private String type;
  private List<InnerBean> inner;

  // getters and setters
}
```

To persist this object, one option is to create a join table and have a row for each element in the list. However, it is often 
simpler and easier to just store this list in a single column, serialized as JSON. Rosetta makes this very easy via its
`@StoredAsJson` annotation. **This annotation just affects Rosetta binding/mapping and won't mess up the JSON representation elsewhere
in your application.**  To use, just annotate the field with `@StoredAsJson` like this:

```java
public class OuterBean {
  private String type;

  @StoredAsJson
  private List<InnerBean> inner;

  // getters and setters
}
```

And then you can write a method that looks like: (assuming `my_table` has a text column called `inner`)
```java
@SqlUpdate("INSERT INTO my_table (type, inner) VALUES (:type, :inner)")
public void insert(@BindWithRosetta OuterBean bean);
```

Rosetta will write the field to JSON and store the JSON string in the `inner` column. And then to fetch the object would look like:
```java
@SqlQuery("SELECT type, inner FROM my_table WHERE type = :type")
public OuterBean retrieve(@Bind("type") String type);
```

Rosetta will deserialize the JSON string back into the list of `InnerBean` and everything should Just Work™. If you prefer to store 
the JSON as a blob rather than a text column, you just need to update the annotation to be `@StoredAsJson(binary = true)`, and then
Rosetta will convert the field to a byte array rather than JSON string.

## Custom Naming Strategies

Assuming your Java field names are camel-case and your SQL column names are lowercase with underscores (a pretty common scenario), you
can allow Rosetta to handle the name transformation by annotating the Java objects with
`@RosettaNaming(LowerCaseWithUnderscoresStrategy.class)` which will change Jackson's naming strategy to lower case with underscores
but only for Rosetta binding/mapping (other Jackson operations throughout your application are unaffected). 

Or to avoid the need to annotate each Java object individually, you can make this configuration change globally by using a Jackson
`Module` that sets the default naming strategy to lowercase with underscores, which would look like:

```java
public class LowerCaseWithUnderscoresModule extends SimpleModule {

  @Override
  public void setupModule(SetupContext context) {
    context.setNamingStrategy(new LowerCaseWithUnderscoresStrategy());
  }
}
```

And then you would need to register this module with the `ObjectMapper` being used for Rosetta operations.

## Providing your own ObjectMapper

By default there is a global singleton `ObjectMapper` that is used for all Rosetta operations, shared across all `DBI` instances.
However, you can supply your own `ObjectMapper` per DBI, per handle, or even per statement. To set the `ObjectMapper` at the `DBI`
level, you would do the following while setting up your `DBI`:  
`new RosettaObjectMapperOverride(myObjectMapper).override(dbi);`

To set at the handle or statement level would look similar:  
`new RosettaObjectMapperOverride(myOtherObjectMapper).override(handle);`  
`new RosettaObjectMapperOverride(myOtherOtherObjectMapper).override(query);`

## Working with hubspot-immutables

`hubspot-immutables` provides a preconfigured [immutables](https://immutables.github.io/) [style](https://immutables.github.io/style.html) for generating immutable pojos, as well as other customizations that make working with immutables easier.  Most of these features will work out of the box with Rosetta, however some depend on customizations of the `ObjectMapper` in order to function properly, which causes problems when interacting with Rosetta.  In order to support these usecases, `RosettaImmutables` provides the [RosettaImmutablesModule](https://github.com/HubSpot/Rosetta/blob/218547991994d631206eff4950f54fe2272c5fd2/RosettaImmutables/src/main/java/com/hubspot/rosetta/immutables/RosettaImmutablesModule.java) that you can use in combination with the `RosettaObjectMapperOverride`.

First add a dependency on the `RosettaImmutables` module

```xml
<dependency>
  <groupId>com.hubspot.rosetta</groupId>
  <artifactId>RosettaImmutables</artifactId>
  <version>whichever version matches your rosetta version</version>
</dependency>
```
Then configure your `ObjectMapper` override

```java
ObjectMapper newRosettaMapper = myOtherObjectMapper
  .copy()
  .registerModule(new RosettaImmutablesModule());

new RosettaObjectMapperOverride(newRosettaMapper).override(handle)
new RosettaObjectMapperOverride(newRosettaMapper).override(query)
```

