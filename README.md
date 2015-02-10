# Rosetta [![Build Status](https://travis-ci.org/HubSpot/Rosetta.svg?branch=master)](https://travis-ci.org/HubSpot/Rosetta)

A library that takes the pain out of data mapping and binding.

## Overview

Rosetta is a Java library that leverages [Jackson](https://github.com/FasterXML/jackson) to take the pain out of mapping objects to/from the DB, designed to integrate seamlessly with [jDBI](https://github.com/jdbi/jdbi). Jackson is extremely fast, endlessly configurable, and already used by many Java webapps. 

Rosetta isn't an ORM. It doesn't silently read and write to your database, validate input, or manage connections. It does only two things:

1. Maps query results (or any other un-typed, map-like result set) to Object fields, and
2. Binds object fields to query parameters (or some non-SQL equivalent of that)

It doesn't sound like much, and it's not. But sometimes Java makes the easiest things the hardest to accomplish.

## Package Structure

The package is split up into sub-modules to avoid the need to pull in unwanted dependencies. **Consult module READMEs for usage instructions**.

* [RosettaAnnotations](RosettaAnnotations/README.md)
* [RosettaCore](RosettaCore/README.md)
* [RosettaJdbi](RosettaJdbi/README.md)
