package com.hubspot.rosetta.jdbi3;

import java.util.List;

import org.jdbi.v3.sqlobject.SqlObject;
import org.jdbi.v3.sqlobject.config.RegisterRowMapperFactory;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

@RegisterRowMapperFactory(RosettaRowMapperFactory.class)
public interface TestDao extends SqlObject {

  @SqlQuery("SELECT * FROM test_table")
  List<TestObject> getAll();

  @SqlUpdate("INSERT INTO test_table (id, name) VALUES (:id, :name)")
  int insert(@BindWithRosetta TestObject object);
}
