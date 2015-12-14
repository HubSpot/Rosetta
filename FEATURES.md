
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
  
  public OuterBean setInner(InnerBean inner) {
    this.inner = inner;
    return this;
  }
}

public class InnerBean {
  private long id;
  private String name;
  
  public long getId() {
    return id;
  }
  
  public InnerBean setId(long id) {
    this.id = id;
    return this;
  }
  
  public String getName() {
    return name;
  }
  
  public InnerBean setName(String name) {
    this.name = name;
    return this;
  }
}
```

You could write a SQL Object method that looked like:

```java
@SqlUpdate("INSERT INTO my_table (id, name) VALUES (:inner.id, :inner.name)")
public void insert(@BindWithRosetta OuterBean bean);
```

On the mapping side, things are slightly trickier. At a conceptual level, for mapping to work the JSON we pass to Jackson needs to
have the following structure, since this is the structure that Jackson expects based on our object graph:
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
