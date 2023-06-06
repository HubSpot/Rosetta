package com.hubspot.rosetta.jdbi3;

import java.util.List;

import org.jdbi.v3.sqlobject.SqlObject;
import org.jdbi.v3.sqlobject.config.RegisterRowMapperFactory;
import org.jdbi.v3.sqlobject.customizer.BindList.EmptyHandling;
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

  @SqlQuery("SELECT * FROM test_list_table WHERE value NOT IN (<values>)")
  List<TestListObject> getWithoutValueEmptyToNull(@BindListWithRosetta(value = "values", onEmpty = EmptyHandling.NULL) List<TestEnum> values);

  @SqlQuery("SELECT * FROM test_list_table WHERE value NOT IN (<values>)")
  List<TestListObject> getWithoutValueEmptyToVoid(@BindListWithRosetta(value = "values", onEmpty = EmptyHandling.VOID) List<TestEnum> values);

  @SqlQuery("SELECT * FROM test_list_table WHERE value IN (<values>)")
  List<TestListObject> getWithFieldValue(@BindListWithRosetta(value = "values", field = "value") List<TestListObject> values);

  @SqlQuery("SELECT * FROM test_list_table WHERE value IN (<values>)")
  List<TestListObject> getWithListFieldValue(@BindListWithRosetta(value = "values", field = "stringValues") List<TestListObject> values);

  @SqlQuery("SELECT * FROM test_list_table WHERE value IN (<values>)")
  List<TestListObject> getWithObjectFieldValue(@BindListWithRosetta(value = "values", field = "objectValue") List<TestListObject> values);

  @SqlQuery("SELECT " +
      "test_table.id AS id, " +
      "test_table.name AS name, " +
      "r.id AS \"related.id\", " +
      "r.relatedId AS \"related.relatedId\", " +
      "r.name AS \"related.name\", " +
      "r.score AS \"related.score\" " +
      "FROM test_table LEFT JOIN test_nested_table as r " +
      "ON test_table.id = r.relatedId;")
  List<TestViewObject> getAllView();

  @SqlUpdate("INSERT INTO test_table (id, name) VALUES (:id, :name)")
  int insert(@BindWithRosetta TestObject object);

  @SqlUpdate("INSERT INTO test_list_table (id, value) VALUES (:id, :value)")
  int insert(@BindWithRosetta TestListObject object);

  @SqlUpdate("INSERT INTO test_nested_table (id, relatedId, name, score) VALUES (:id, :relatedId, :name, :score);")
  int insert(@BindWithRosetta TestRelatedObject object);
}
