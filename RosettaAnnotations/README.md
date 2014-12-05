# RosettaAnnotations

This module has no dependencies and provides a few annotations that can be used to change the way Rosetta maps/binds your objects (without these changes affecting their JSON representation). A good example is using `@RosettaValue` on an enum because you store ints in the DB, but you still want to return the enum names in the JSON.

