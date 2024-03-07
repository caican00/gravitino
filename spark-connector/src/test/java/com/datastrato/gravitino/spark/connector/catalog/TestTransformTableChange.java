/*
 *  Copyright 2024 Datastrato Pvt Ltd.
 *  This software is licensed under the Apache License version 2.
 */

package com.datastrato.gravitino.spark.connector.catalog;

import java.util.Arrays;
import org.apache.spark.sql.connector.catalog.TableChange;
import org.apache.spark.sql.types.DataTypes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestTransformTableChange {

  @Test
  void testTransformAddColumn() {
    TableChange sparkChange = TableChange.addColumn(new String[] {"col1"}, DataTypes.StringType);
    com.datastrato.gravitino.rel.TableChange gravitinoChange =
        GravitinoCatalog.transformTableChange(sparkChange);

    TableChange.AddColumn sparkAddColumn = (TableChange.AddColumn) sparkChange;
    Assertions.assertTrue(
        gravitinoChange instanceof com.datastrato.gravitino.rel.TableChange.AddColumn);
    com.datastrato.gravitino.rel.TableChange.AddColumn gravitinoAddColumn =
        (com.datastrato.gravitino.rel.TableChange.AddColumn) gravitinoChange;

    Assertions.assertEquals(1, sparkAddColumn.fieldNames().length);
    Assertions.assertEquals(1, gravitinoAddColumn.fieldName().length);
    Assertions.assertEquals(
        Arrays.stream(sparkAddColumn.fieldNames()).findFirst(),
        Arrays.stream(gravitinoAddColumn.fieldName()).findFirst());
    Assertions.assertEquals(sparkAddColumn.dataType(), gravitinoAddColumn.getDataType());
  }

  @Test
  void testTransformDeleteColumn() {
    TableChange sparkChange = TableChange.deleteColumn(new String[] {"col1"}, true);
    com.datastrato.gravitino.rel.TableChange gravitinoChange =
        GravitinoCatalog.transformTableChange(sparkChange);

    TableChange.DeleteColumn sparkDeleteColumn = (TableChange.DeleteColumn) sparkChange;
    Assertions.assertTrue(
        gravitinoChange instanceof com.datastrato.gravitino.rel.TableChange.DeleteColumn);
    com.datastrato.gravitino.rel.TableChange.DeleteColumn gravitinoDeleteColumn =
        (com.datastrato.gravitino.rel.TableChange.DeleteColumn) gravitinoChange;

    Assertions.assertEquals(1, sparkDeleteColumn.fieldNames().length);
    Assertions.assertEquals(1, gravitinoDeleteColumn.fieldName().length);
    Assertions.assertEquals(
        Arrays.stream(sparkDeleteColumn.fieldNames()).findFirst(),
        Arrays.stream(gravitinoDeleteColumn.fieldName()).findFirst());
    Assertions.assertEquals(sparkDeleteColumn.ifExists(), gravitinoDeleteColumn.getIfExists());
  }
}
