# RosettaAnnotations

This module has no dependencies and provides a few annotations that can be used to change the way Rosetta maps/binds your objects (without these changes affecting their JSON representation). A good example is using `@RosettaValue` on an enum because you store ints in the DB, but you still want to return the enum names in the JSON.

### Maven dependency

To use module on Maven-based projects, use following dependency:

```xml
<dependency>
  <groupId>com.hubspot.rosetta</groupId>
  <artifactId>RosettaAnnotations</artifactId>
  <version>3.4</version>
</dependency>
```

(or whatever version is most up-to-date at the moment)
