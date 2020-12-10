package org.apache.ibatis.builder;

import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;

import java.util.Properties;

@Intercepts({})
public class ExamplePlugin implements Interceptor {
  private Properties properties;

  @Override
  public Object intercept(Invocation invocation) throws Throwable {
    System.out.println("-----StatementHandlerPlugin-----intercept---------");
    return invocation.proceed();
  }

  @Override
  public Object plugin(Object target) {
    System.out.println("-----StatementHandlerPlugin-plugin-------------");
    return Plugin.wrap(target, this);
  }

  @Override
  public void setProperties(Properties properties) {
    System.out.println("-----StatementHandlerPlugin---setProperties-----------");
    this.properties = properties;
  }

  public Properties getProperties() {
    System.out.println("-----StatementHandlerPlugin-----getProperties---------");
    return properties;
  }

}
