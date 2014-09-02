# Rosetta [![Build Status](https://travis-ci.org/HubSpot/Rosetta.svg?branch=master)](https://travis-ci.org/HubSpot/Rosetta)

A library that takes the pain out of data mapping and binding.

## Overview

> "If you don't need to edit at least 5 files to usher data from a form to the db you're doing it wrong" -James Kebinger

Add a column to your DB schema and map it to an object field. Go ahead, I'll wait.

That was quick! Okay, now do it again, except make the column an `int` and the field an `Enum`. Once you've written those hundred lines, how about you go ahead and add a join column like `parentId` and map it to a field of type `Parent`. If you haven't killed yourself yet, you might like Rosetta.

> I have a theory that nobody really wants an ORM, they just want simple mapping between objects and query params/results and transactions. -Me

Rosetta isn't an ORM. It doesn't silently read and write to your database, validate input, or manage connections. It does only two things:

1. Maps query results (or any other un-typed, map-like result set) to Object fields, and
2. Binds object fields to query parameters (or some non-SQL equivalent of that)

It doesn't sound like much, and it's not. But sometimes Java makes the easiest things the hardest to accomplish.

## Package Structure

The package is split up into sub-modules to avoid the need to pull in unwanted dependencies. **Consult module READMEs for usage instructions**.

### RosettaCore

This module contains core Rosetta logic for mapping `ResultSet` (or more general `Map<String, String>`) to bona fide objects. Its README covers built-in mapping features and available customization.

### RosettaJdbi

Classes specific to JDBI: `ResultSetMapperFactory` and (future) `@BindBean` equivalent with all the standard Rosetta goodies.
