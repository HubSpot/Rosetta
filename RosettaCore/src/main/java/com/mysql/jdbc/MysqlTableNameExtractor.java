package com.mysql.jdbc;

import com.hubspot.rosetta.internal.TableNameExtractor;

import java.sql.SQLException;

/**
 * This is to work around a deficiency in the JDBC API.
 * There is only one method to get a table name: ResultSetMetaData#getTableName
 * but a table can be alias'ed. MySQL driver used to return the alias'ed table
 * name from this method but now it returns the unaliased version. This
 * implements a mysql-specific workaround
 *
 * Extends {@link ResultSetMetaData} so that {@link ResultSetMetaData#getField(int)}
 * is accessible, even if both classes are loaded by different class loaders (workaround
 * for the issue described <a href="http://stackoverflow.com/a/3387520/798498">here</a>)
 */
public class MysqlTableNameExtractor extends ResultSetMetaData implements TableNameExtractor {

  public MysqlTableNameExtractor() {
    super(new Field[0], false, false, null);
  }

  @Override
  public String getTableName(java.sql.ResultSetMetaData metaData, int columnIndex) throws SQLException {
    if (metaData instanceof ResultSetMetaData) {
      ResultSetMetaData mysqlMetaData = (ResultSetMetaData) metaData;
      return mysqlMetaData.getField(columnIndex).getTableName();
    } else {
      return metaData.getTableName(columnIndex);
    }
  }
}
