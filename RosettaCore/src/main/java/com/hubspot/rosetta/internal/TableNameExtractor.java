package com.hubspot.rosetta.internal;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public interface TableNameExtractor {
  String getTableName(ResultSetMetaData metaData, int columnIndex) throws SQLException;

  class Default implements TableNameExtractor {

    @Override
    public String getTableName(ResultSetMetaData metaData, int columnIndex)
      throws SQLException {
      return metaData.getTableName(columnIndex);
    }
  }
}
