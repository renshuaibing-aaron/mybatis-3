package org.mybatis.spring.mapper;

import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.session.Configuration;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.FactoryBean;

import static org.springframework.util.Assert.notNull;

/**
 * BeanFactory that enables injection of MyBatis mapper interfaces. It can be set up with a
 * SqlSessionFactory or a pre-configured SqlSessionTemplate.
 * <p>
 * Sample configuration:
 *
 * <pre class="code">
 * {@code
 *   <bean id="baseMapper" class="org.mybatis.spring.mapper.MapperFactoryBean" abstract="true" lazy-init="true">
 *     <property name="sqlSessionFactory" ref="sqlSessionFactory" />
 *   </bean>
 *
 *   <bean id="oneMapper" parent="baseMapper">
 *     <property name="mapperInterface" value="my.package.MyMapperInterface" />
 *   </bean>
 *
 *   <bean id="anotherMapper" parent="baseMapper">
 *     <property name="mapperInterface" value="my.package.MyAnotherMapperInterface" />
 *   </bean>
 * }
 * </pre>
 * <p>
 * Note that this factory can only inject <em>interfaces</em>, not concrete classes.
 *
 * @author Eduardo Macarron
 *
 * @see SqlSessionTemplate
 */
public class MapperFactoryBean<T> extends SqlSessionDaoSupport implements FactoryBean<T> {
  /**
   * Mapper 接口
   */
  private Class<T> mapperInterface;
  /**
   * 是否添加到 {@link Configuration} 中
   */
  private boolean addToConfig = true;

  public MapperFactoryBean() {
    //intentionally empty
  }

  public MapperFactoryBean(Class<T> mapperInterface) {
    this.mapperInterface = mapperInterface;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void checkDaoConfig() {
    //其实这个方法是在InitializingBean#afterPropertiesSet() 的方法中实现的
    System.out.println("【MapperFactoryBean#checkDaoConfig】");

    // <1> 校验 sqlSessionTemplate 非空
    super.checkDaoConfig();

    // <2> 校验 mapperInterface 非空
    notNull(this.mapperInterface, "Property 'mapperInterface' is required");

    // <3> 添加 Mapper 接口到 configuration 中
    Configuration configuration = getSqlSession().getConfiguration();
    if (this.addToConfig && !configuration.hasMapper(this.mapperInterface)) {
      try {
        configuration.addMapper(this.mapperInterface);
      } catch (Exception e) {
        logger.error("Error while adding the mapper '" + this.mapperInterface + "' to configuration.", e);
        throw new IllegalArgumentException(e);
      } finally {
        ErrorContext.instance().reset();
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public T getObject() throws Exception {
    System.out.println("【MapperFactoryBean#getObject() 获取mapper的代理类】");
    return getSqlSession().getMapper(this.mapperInterface);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Class<T> getObjectType() {
    return this.mapperInterface;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isSingleton() {
    return true;
  }

  //------------- mutators --------------

  /**
   * Sets the mapper interface of the MyBatis mapper
   *
   * @param mapperInterface class of the interface
   */
  public void setMapperInterface(Class<T> mapperInterface) {
    this.mapperInterface = mapperInterface;
  }

  /**
   * Return the mapper interface of the MyBatis mapper
   *
   * @return class of the interface
   */
  public Class<T> getMapperInterface() {
    return mapperInterface;
  }

  /**
   * If addToConfig is false the mapper will not be added to MyBatis. This means
   * it must have been included in mybatis-config.xml.
   * <p/>
   * If it is true, the mapper will be added to MyBatis in the case it is not already
   * registered.
   * <p/>
   * By default addToCofig is true.
   *
   * @param addToConfig
   */
  public void setAddToConfig(boolean addToConfig) {
    this.addToConfig = addToConfig;
  }

  /**
   * Return the flag for addition into MyBatis config.
   *
   * @return true if the mapper will be added to MyBatis in the case it is not already
   * registered.
   */
  public boolean isAddToConfig() {
    return addToConfig;
  }
}