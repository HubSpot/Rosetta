# RosettaCore

This module contains the core mapping and binding logic used throughout.

## Mapping vs. Binding

Mapping is used when mapping fields and values *to* an object.

Binding is a method for mapping object fields *to* (usually) SQL query parameters.


## Mapping Features

Because Rosetta is built on `jackson-databind`, you can use all the [Annotations](http://wiki.fasterxml.com/JacksonAnnotations) you know and love. Let's cover one common customization: `Enum` handling. Storing an enum in the database by name (ordinal, etc.) is less efficient, less maintainable, and affords fewer queries than a representation like `tinyint`. Say you have leads with some states:

```java
enum LeadState {
  VISITED(0),
  STARRED(1),
  EMAILED(2),
  BOUGHT(3)

  final int state;

  LeadState(int state) {
    this.state = state;
  }

  public final int getState() {
    return state;
  }


}
```

Standard stuff. And finding all pending leads is as simple as `select * from leads where state < 3`. But we still need to tell Rosetta how to translate the integer (from the database) to the enum value (on our pretend `Lead` class). Here's all it takes:

```java
enum LeadState {

  @RosettaValue
  public final int getState() {
    return state;
  }

  @RosettaCreator
  public static LeadState fromInt(int state) {
    switch (approvalStatus) {
      case 0: return VISITED;
      case 1: return STARRED;
      case 2: return EMAILED;
      case 3: return BOUGHT;
      default: VISITED;
    }
  }
}
```

### Mapping Joins/Unions

What if leads could refer *other* leads?

```java
class Lead {
  // Bunch of fields...

  @Joinable(as="r")
  Lead referrer;
}
```

Now a query like this will work as you might expect:

```sql
SELECT lead.*, ref.name as `r.name`
FROM `leads` lead
LEFT JOIN `leads` ref
ON lead.referrerId = ref.id;
```

(The alias prefix defaults to the name of the field, so `as="r"` is optional)

## Mapping API

Although it's rare you'll use it independently, the primary API consists of these (documented) classes:

* `RosettaMapperFactory`
* `RosettaMapper`

## Binding Features

The binding side of the house affords the same functionality, though generally through more context-specific modules such as `RosettaJdbi`. It utilizes the same Jackson annotations, supports mapping nested object fields to prefixed query parameters, etc.

For example, consider the following classes (infer getters/setters):

```java
public class Parent {
  String name;
  @Joinable Person child;
}

public class Person {
  String name;
  int age;
  Gender gender;
}

enum Gender {
  MALE(0),
  FEMALE(1),

  int digit;

  @JsonValue
  public int forDb() {
    return digit;
  }
}
```

Calling `RosettaBinder#makeBoundMap(parentInstance)` would result in a Map with the structure:

```json
{
  "name": "Jill",
  "child.age": 15,
  "child.gender": 0,
  "child.name": "Bill"
}
```
