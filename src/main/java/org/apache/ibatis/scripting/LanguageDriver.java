package org.apache.ibatis.scripting;

import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;
import org.apache.ibatis.session.Configuration;

/**
 * 语言驱动接口
 */
public interface LanguageDriver {

  /**
   * Creates a {@link ParameterHandler} that passes the actual parameters to the the JDBC statement.
   *  创建 ParameterHandler 对象。
   * @param mappedStatement The mapped statement that is being executed
   * @param parameterObject The input parameter object (can be null)
   * @param boundSql The resulting SQL once the dynamic language has been executed.
   * @return
   * @author Frank D. Martinez [mnesarco]
   * @see DefaultParameterHandler
   */
  ParameterHandler createParameterHandler(MappedStatement mappedStatement, Object parameterObject, BoundSql boundSql);

  /**
   * Creates an {@link SqlSource} that will hold the statement read from a mapper annotation file.
   * It is called during startup, when the mapped statement is read from a class or an annotation file.
   *  创建 SqlSource 对象，从 Mapper XML 配置的 Statement 标签中，即 <select /> 等。
   * @param configuration The MyBatis configuration
   * @param script XNode parsed from a XML file
   * @param parameterType input parameter type got from a mapper method or specified in the parameterType annotation attribute. Can be null.
   * @return
   */
  SqlSource createSqlSource(Configuration configuration, XNode script, Class<?> parameterType);

  /**
   * Creates an {@link SqlSource} that will hold the statement read from an xmltype.
   * It is called during startup, when the mapped statement is read from a class or an annotation file.
   *  创建 SqlSource 对象，从方法注解配置，即 @Select 等。
   * @param configuration The MyBatis configuration
   * @param script The content of the xmltype
   * @param parameterType input parameter type got from a mapper method or specified in the parameterType annotation attribute. Can be null.
   * @return
   */
  SqlSource createSqlSource(Configuration configuration, String script, Class<?> parameterType);

}
