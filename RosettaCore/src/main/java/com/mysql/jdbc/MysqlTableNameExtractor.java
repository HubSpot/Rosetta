package com.mysql.jdbc;

import com.hubspot.rosetta.internal.TableNameExtractor;

import java.sql.SQLException;

/**
 * This is to work around a deficiency in the JDBC API.
 * There is only one method to get a table name: ResultSetMetaData#getTableName
 * but a table can be alias'ed. MySQL driver used to return the alias'ed table
 * name from this method but now it returns the unaliased version. This
 * implements a mysql-specific workaround
 */
public class MysqlTableNameExtractor implements TableNameExtractor {

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
