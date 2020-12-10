package org.apache.ibatis.datasource;

import java.util.Properties;
import javax.sql.DataSource;

/**
 * 数据源设置
 * @author Clinton Begin
 */
public interface DataSourceFactory {

  /**
   * 设置 DataSource 对象的属性
   *
   * @param props 属性
   */
  void setProperties(Properties props);

  /**
   * 获得 DataSource 对象
   *
   * @return DataSource 对象
   */
  DataSource getDataSource();

}
