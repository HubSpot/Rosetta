
# Rosetta Features

## Nested Objects

Rosetta supports binding and mapping of arbitrarily nested object graphs. Dot-notation is used to separate fields of subobjects. For
example, given these objects:

```java
public class OuterBean {
  private InnerBean inner;

  public InnerBean getInner() {
    return inner;
  }
  
  public void setInner(InnerBean inner) {
    this.inner = inner;
  }
}

public class InnerBean {
  private long id;
  private String name;
  
  public long getId() {
    return id;
  }
  
  public void setId(long id) {
    this.id = id;
  }
  
  public String getName() {
    return name;
  }
  
  public void setName(String name) {
    this.name = name;
  }
}
```

You could write a SQL Object method that looked like:

```java
@SqlUpdate("INSERT INTO my_table (id, name) VALUES (:inner.id, :inner.name)")
public void insert(@BindWithRosetta OuterBean bean);
```

On the deserialization side, things are slightly trickier. At a conceptual level, for mapping to work the JSON we pass to Jackson
needs to have the following structure, since this is the expected structure based on our object graph:
```json
{
  "inner": {
    "id": 123,
    "name": "derp"
  }
}
```

When mapping a `ResultSet`, Rosetta creates two entries for each column. One for the column name (or alias), and one for the column
name prefixed with the table name. So in our previous example, if we wrote a SQL Object method such as:
```java
@SqlQuery("SELECT id, name FROM my_table WHERE id = :id")
public OuterBean retrieve(@Bind("id") long id);
```

We would end up with a JSON structure that looked like:
```json
{
  "id": 123,
  "name": "derp",
  "my_table": {
    "id": 123,
    "name": "derp"
  }
}
```

Which won't deserialize correctly because it doesn't match our object structure. To make this work we have two options. The first is
to alias the column names in the select statement, which would look like this:
```java
@SqlQuery("SELECT id AS `inner.id`, name AS `inner.name` FROM my_table WHERE id = :id")
public OuterBean retrieve(@Bind("id") long id);
```

Which would generate the following JSON structure:
```json
{
  "inner": {
    "id": 123,
    "name": "derp"
  },
  "my_table": {
    "id": 123,
    "name": "derp"
  }
}
```
Which would deserialize correctly (the unknown `my_table` field would be ignored by Jackson because we configure the `ObjectMapper` to
ignore uknown fields).

The other option is to alias the table rather than the column, which would look like this:
```java
@SqlQuery("SELECT id, name FROM my_table AS inner WHERE id = :id")
public OuterBean retrieve(@Bind("id") long id);
```

Which would generate the following JSON structure:
```json
{
  "id": 123,
  "name": "derp",
  "inner": {
    "id": 123,
    "name": "derp"
  }
}
```

Which would also deserialize correctly. So if you're trying to map a complex object graph, remember how Rosetta generates the JSON
structure and you should be able to use column and/or table aliasing to make it work.

## Fields stored as JSON

Let's say we modify the objects from the previous example so that `OuterBean` now contains a list of `InnerBean` 
(`InnerBean` definition is unchanged):

```java
public class OuterBean {
  private List<InnerBean> inner;

  public List<InnerBean> getInner() {
    return inner;
  }
  
  public void setInner(List<InnerBean> inner) {
    this.inner = inner;
  }
}
```

To persist this object, one option is to create a join table and have a row for each element in the list. However, it is often 
simpler and easier to just store this list in a single column, serialized as JSON. Rosetta makes this very easy via its
`@StoredAsJson` annotation. **This annotation just affects Rosetta binding/mapper and won't mess up the JSON representation elsewhere
in your application.**  To use, just annotate the field with `@StoredAsJson` like this:

```java
public class OuterBean {
  @StoredAsJson
  private List<InnerBean> inner;

  public List<InnerBean> getInner() {
    return inner;
  }
  
  public void setInner(List<InnerBean> inner) {
    this.inner = inner;
  }
}
```

And then you can write a SQL Object method that looks like: (assuming `my_table` has a text column called `inner`)
```java
@SqlUpdate("INSERT INTO my_table (inner) VALUES (:inner)")
public void insert(@BindWithRosetta OuterBean bean);
```

Rosetta will write the field to JSON and store the JSON string in the `inner` column. And then to fetch the object would look like:
```java
@SqlQuery("SELECT inner FROM my_table")
public OuterBean retrieve();
```

Rosetta will deserialize the JSON string back into the list of `InnerBean` and everything should Just Work™. If you prefer to store 
the JSON as a blob rather than a text column, you just need to update the annotation to be `@StoredAsJson(binary = true)`, and then
Rosetta will convert the field to a byte array rather than JSON string.

## Configuring the ObjectMapper

By default there is a global singleton `ObjectMapper` that is used for all Rosetta operations, shared across all `DBI` instances.
However, you can supply your own `ObjectMapper` per DBI, per handle, or even per statement. To set the `ObjectMapper` at the `DBI`
level, you would do the following while setting up your `DBI`:  
`new RosettaObjectMapperOverride(myObjectMapper).override(dbi);`

To set at the handle or statement level would look similar:  
`new RosettaObjectMapperOverride(myOtherObjectMapper).override(handle);`  
`new RosettaObjectMapperOverride(myOtherOtherObjectMapper).override(query);`

## Different naming strategy

Assuming your Java field names are camel-case and your SQL column names are lowercase with underscores (a pretty common scenario), you
can make this work with Rosetta by annotating the Java objects with `@RosettaNaming(LowerCaseWithUnderscoresStrategy.class)` which
will change Jackson's naming strategy to lower case with underscores but only for Rosetta binding/mapping (other Jackson operations
throughout your application are unaffected). 

Or to avoid the need to annotate each Java object individually, you can make this configuration change globally for all Rosetta
operations by using a Jackson `Module` that sets the default naming strategy to lowercase with underscores, which would look like:

```java
public class LowerCaseWithUnderscoresModule extends SimpleModule {

  @Override
  public void setupModule(SetupContext context) {
    context.setNamingStrategy(new LowerCaseWithUnderscoresStrategy());
  }
}
```

You would then register this module with the `ObjectMapper` being used for Rosetta operations.
