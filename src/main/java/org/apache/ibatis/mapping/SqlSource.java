package org.apache.ibatis.mapping;

/**
 * SQLSOURCE sql来源接口 代表了从xml或者注解上读取的一条sql内容
 * Represents the content of a mapped statement read from an XML file or an xmltype.
 * It creates the SQL that will be passed to the database out of the input parameter received from the user.
 *
 * @author Clinton Begin
 */
public interface SqlSource {

  /**
   * 根据传入的参数对象，返回 BoundSql 对象
   * @param parameterObject
   * @return
   */
  BoundSql getBoundSql(Object parameterObject);

}
