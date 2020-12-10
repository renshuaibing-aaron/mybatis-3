package com.aaron.ren.plugin;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;

import java.sql.Connection;
import java.util.Properties;

@Intercepts(
  @Signature(
    type = StatementHandler.class,
    method = "prepare",
    args = {Connection.class, Integer.class}
  )
)
public class StatementHandlerPlugin implements Interceptor {
  private Properties properties;

  @Override
  public Object intercept(Invocation invocation) throws Throwable {
    // 获取StatementHandler对象
    StatementHandler statementHandler = (StatementHandler) invocation.getTarget();

    // 通过SystemMetaObject获取元信息
    MetaObject metaObject = SystemMetaObject.forObject(statementHandler);

    String sql = (String) metaObject.getValue("delegate.boundSql.sql");

    System.out.println("==========原生的sql=============="+sql);
    // 在原SQL后面添加LIMIT
    if (sql != null && sql.startsWith("SELECT")) {
      sql = sql + "LIMIT " + properties.getProperty("row");
      metaObject.setValue("delegate.boundSql.sql", sql);
    }

    return invocation.proceed();
  }

  @Override
  public Object plugin(Object target) {
    // 一般来说，在plugin方法中只需要这一行代码就够了
    // 因为Plugin.wrap会根据@Intercepts的注解判断是返回原对象还是返回包装后的对象
    return Plugin.wrap(target, this);
  }

  // 此代码在MyBatis上下文初始化的时候就会调用
  @Override
  public void setProperties(Properties properties) {
    // 展示配置的properties
    properties.forEach((key, value) -> System.out.println("key=" + key + ", value=" + value));
    this.properties = properties;
  }
}
