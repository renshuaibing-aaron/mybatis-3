package com.aaron.ren.spring.annotation;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
@MapperScan("com.aaron.ren.spring.annotation")//扫描mapper
@ComponentScan("com.aaron.ren.spring.annotation") //这两个不同
public class AppConfig {

  /**
   * 数据源
   * @return
   */
  @Bean
  public DataSource getDataSource(){
    DriverManagerDataSource dataSource = new DriverManagerDataSource();
    dataSource.setDriverClassName("com.mysql.jdbc.Driver");
    dataSource.setUrl("jdbc:mysql://10.1.168.56:3306/activity?useUnicode=true&characterEncoding=utf-8");
    dataSource.setUsername("root");
    dataSource.setPassword("Abc12345");
    return dataSource;
  }

  /**
   * sqlSessionFactoryBean
   * @param dataSource
   * @return
   */
  @Bean
  public SqlSessionFactoryBean getSqlSessionFactoryBean(DataSource dataSource){
    SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
    sqlSessionFactoryBean.setDataSource(dataSource);
    return sqlSessionFactoryBean;
  }
}
