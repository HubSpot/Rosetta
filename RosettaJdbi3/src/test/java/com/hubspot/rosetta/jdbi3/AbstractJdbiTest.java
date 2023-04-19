package com.hubspot.rosetta.jdbi3;

import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import org.junit.Before;

public class AbstractJdbiTest {
  private Jdbi jdbi;

  @Before
  public void setup() {
    jdbi = Jdbi.create("jdbc:h2:mem:rosetta;DB_CLOSE_DELAY=-1;DATABASE_TO_UPPER=FALSE");
    jdbi.installPlugin(new SqlObjectPlugin());
    jdbi.registerRowMapper(new RosettaRowMapperFactory());
    jdbi.useHandle(handle -> {
      handle.execute("CREATE TABLE IF NOT EXISTS test_table (id INT, name VARCHAR(255) NOT NULL, PRIMARY KEY (id))");
      handle.execute("CREATE TABLE IF NOT EXISTS test_list_table (id INT, value INT NOT NULL, PRIMARY KEY (id))");
      handle.execute("TRUNCATE TABLE test_table");
      handle.execute("TRUNCATE TABLE test_list_table");
    });
  }

  protected Jdbi getJdbi() {
    return jdbi;
  }

  protected TestDao getDao() {
    return jdbi.onDemand(TestDao.class);
  }
}
