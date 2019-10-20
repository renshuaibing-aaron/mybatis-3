package com.aaron.ren.spring;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
@MapperScan("com.learn.mybatis_info.mybatis_info.mappers")//扫描mapper
public class AppConfig {

  /**
   * 数据源
   * @return
   */
  @Bean
  public DataSource getDataSource(){
    DriverManagerDataSource dataSource = new DriverManagerDataSource();
    dataSource.setDriverClassName("com.mysql.jdbc.Driver");
    dataSource.setUrl("jdbc:mysql://localhost:3306/mybatis?characterEncoding=utf-8&useSSL=false&serverTimezone=UTC");
    dataSource.setUsername("root");
    dataSource.setPassword("123456");
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
