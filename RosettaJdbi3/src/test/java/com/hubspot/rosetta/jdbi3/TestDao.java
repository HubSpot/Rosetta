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

  @SqlQuery("SELECT * FROM test_list_table")
  List<TestListObject> getAllList();

  @SqlQuery("SELECT * FROM test_list_table WHERE value IN (<values>)")
  List<TestListObject> getWithValue(@BindListWithRosetta("values") List<TestEnum> values);

  @SqlQuery("SELECT * FROM test_list_table WHERE value IN (<values>)")
  List<TestListObject> getWithFieldValue(@BindListWithRosetta(value = "values", field = "value") List<TestListObject> values);

  @SqlUpdate("INSERT INTO test_table (id, name) VALUES (:id, :name)")
  int insert(@BindWithRosetta TestObject object);

  @SqlUpdate("INSERT INTO test_list_table (id, value) VALUES (:id, :value)")
  int insert(@BindWithRosetta TestListObject object);
}
