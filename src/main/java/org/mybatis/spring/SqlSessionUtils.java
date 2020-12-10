package org.mybatis.spring;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.transaction.SpringManagedTransactionFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.TransientDataAccessResourceException;
import org.springframework.dao.support.PersistenceExceptionTranslator;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import static org.springframework.util.Assert.notNull;

/**
 * org.mybatis.spring.SqlSessionUtils ，SqlSession 工具类。它负责处理 MyBatis SqlSession 的生命周期。
 * 它可以从 Spring TransactionSynchronizationManager 中，注册和获得对应的 SqlSession 对象。同时，它也支持当前不处于事务的情况下
 *
 * Handles MyBatis SqlSession life cycle. It can register and get SqlSessions from
 * Spring {@code TransactionSynchronizationManager}. Also works if no transaction is active.
 *
 * @author Hunter Presnall
 * @author Eduardo Macarron
 */
public final class SqlSessionUtils {

  private static final Log LOGGER = LogFactory.getLog(SqlSessionUtils.class);

  private static final String NO_EXECUTOR_TYPE_SPECIFIED = "No ExecutorType specified";
  private static final String NO_SQL_SESSION_FACTORY_SPECIFIED = "No SqlSessionFactory specified";
  private static final String NO_SQL_SESSION_SPECIFIED = "No SqlSession specified";

  /**
   * This class can't be instantiated, exposes static utility methods only.
   */
  private SqlSessionUtils() {
    // do nothing
  }

  /**
   * Creates a new MyBatis {@code SqlSession} from the {@code SqlSessionFactory}
   * provided as a parameter and using its {@code DataSource} and {@code ExecutorType}
   *
   * @param sessionFactory a MyBatis {@code SqlSessionFactory} to create new sessions
   * @return a MyBatis {@code SqlSession}
   * @throws TransientDataAccessResourceException if a transaction is active and the
   *             {@code SqlSessionFactory} is not using a {@code SpringManagedTransactionFactory}
   */
  public static SqlSession getSqlSession(SqlSessionFactory sessionFactory) {
    System.out.println("【SqlSessionUtils#getSqlSession】");
    // 获得执行器类型
    ExecutorType executorType = sessionFactory.getConfiguration().getDefaultExecutorType();
    // 获得 SqlSession 对象
    return getSqlSession(sessionFactory, executorType, null);
  }

  /**
   * Gets an SqlSession from Spring Transaction Manager or creates a new one if needed.
   * Tries to get a SqlSession out of current transaction. If there is not any, it creates a new one.
   * Then, it synchronizes the SqlSession with the transaction if Spring TX is active and
   * <code>SpringManagedTransactionFactory</code> is configured as a transaction manager.
   *
   * @param sessionFactory a MyBatis {@code SqlSessionFactory} to create new sessions
   * @param executorType The executor type of the SqlSession to create
   * @param exceptionTranslator Optional. Translates SqlSession.commit() exceptions to Spring exceptions.
   * @throws TransientDataAccessResourceException if a transaction is active and the
   *             {@code SqlSessionFactory} is not using a {@code SpringManagedTransactionFactory}
   * @see SpringManagedTransactionFactory
   */
  public static SqlSession getSqlSession(SqlSessionFactory sessionFactory, ExecutorType executorType, PersistenceExceptionTranslator exceptionTranslator) {

    notNull(sessionFactory, NO_SQL_SESSION_FACTORY_SPECIFIED);
    notNull(executorType, NO_EXECUTOR_TYPE_SPECIFIED);

    // <1> 获得 SqlSessionHolder 对象
    SqlSessionHolder holder = (SqlSessionHolder) TransactionSynchronizationManager.getResource(sessionFactory);
    // <2.1> 获得 SqlSession 对象
    SqlSession session = sessionHolder(executorType, holder);
    if (session != null) {
      return session;
    }

    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug("Creating a new SqlSession");
    }
    // <3.1> 创建 SqlSession 对象
    session = sessionFactory.openSession(executorType);
    // <3.2> 注册到 TransactionSynchronizationManager 中
    registerSessionHolder(sessionFactory, executorType, exceptionTranslator, session);

    return session;
  }

  /**
   * Register session holder if synchronization is active (i.e. a Spring TX is active).
   *
   * Note: The DataSource used by the Environment should be synchronized with the
   * transaction either through DataSourceTxMgr or another tx synchronization.
   * Further assume that if an exception is thrown, whatever started the transaction will
   * handle closing / rolling back the Connection associated with the SqlSession.
   *
   * @param sessionFactory sqlSessionFactory used for registration.
   * @param executorType executorType used for registration.
   * @param exceptionTranslator persistenceExceptionTranslater used for registration.
   * @param session sqlSession used for registration.
   */
  private static void registerSessionHolder(SqlSessionFactory sessionFactory, ExecutorType executorType,
      PersistenceExceptionTranslator exceptionTranslator, SqlSession session) {
    SqlSessionHolder holder;
    if (TransactionSynchronizationManager.isSynchronizationActive()) {
      Environment environment = sessionFactory.getConfiguration().getEnvironment();

      // <1> 如果使用 Spring 事务管理器
      if (environment.getTransactionFactory() instanceof SpringManagedTransactionFactory) {
        System.out.println("【使用spring的事务管理器】");
        if (LOGGER.isDebugEnabled()) {
          LOGGER.debug("Registering transaction synchronization for SqlSession [" + session + "]");
        }
        // <1.1> 创建 SqlSessionHolder 对象
        holder = new SqlSessionHolder(session, executorType, exceptionTranslator);
        // <1.2> 绑定到 TransactionSynchronizationManager 中
        //注意这个key就是就创建 sqlSession 的 SqlSessionFactory 对象
        TransactionSynchronizationManager.bindResource(sessionFactory, holder);
        // <1.3> 创建 SqlSessionSynchronization 到 TransactionSynchronizationManager 中
        TransactionSynchronizationManager.registerSynchronization(new SqlSessionSynchronization(holder, sessionFactory));
        // <1.4> 设置同步
        holder.setSynchronizedWithTransaction(true);
        // <1.4> 设置同步
        holder.requested();
      } else {
        // <2> 如果非 Spring 事务管理器，抛出 TransientDataAccessResourceException 异常
        if (TransactionSynchronizationManager.getResource(environment.getDataSource()) == null) {
          if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("SqlSession [" + session + "] was not registered for synchronization because DataSource is not transactional");
          }
        } else {
          throw new TransientDataAccessResourceException(
              "SqlSessionFactory must be using a SpringManagedTransactionFactory in order to use Spring transaction synchronization");
        }
      }
    } else {
      if (LOGGER.isDebugEnabled()) {
        LOGGER.debug("SqlSession [" + session + "] was not registered for synchronization because synchronization is not active");
      }
    }
}

  private static SqlSession sessionHolder(ExecutorType executorType, SqlSessionHolder holder) {
    SqlSession session = null;
    if (holder != null && holder.isSynchronizedWithTransaction()) {
      // 如果执行器类型发生了变更，抛出 TransientDataAccessResourceException 异常
      if (holder.getExecutorType() != executorType) {
        throw new TransientDataAccessResourceException("Cannot change the ExecutorType when there is an existing transaction");
      }
      // <1> 增加计数
      //这个的计数，是用于关闭 SqlSession 时使用。
      holder.requested();

      if (LOGGER.isDebugEnabled()) {
        LOGGER.debug("Fetched SqlSession [" + holder.getSqlSession() + "] from current transaction");
      }
      // <2> 获得 SqlSession 对象
      session = holder.getSqlSession();
    }
    return session;
  }

  /**
   * Checks if {@code SqlSession} passed as an argument is managed by Spring {@code TransactionSynchronizationManager}
   * If it is not, it closes it, otherwise it just updates the reference counter and
   * lets Spring call the close callback when the managed transaction ends
   *
   * @param session
   * @param sessionFactory
   */
  public static void closeSqlSession(SqlSession session, SqlSessionFactory sessionFactory) {
    notNull(session, NO_SQL_SESSION_SPECIFIED);
    notNull(sessionFactory, NO_SQL_SESSION_FACTORY_SPECIFIED);
    // <1> 从 TransactionSynchronizationManager 中，获得 SqlSessionHolder 对象
    SqlSessionHolder holder = (SqlSessionHolder) TransactionSynchronizationManager.getResource(sessionFactory);
    // <2.1> 如果相等，说明在 Spring 托管的事务中，则释放 holder 计数
    if ((holder != null) && (holder.getSqlSession() == session)) {
      if (LOGGER.isDebugEnabled()) {
        LOGGER.debug("Releasing transactional SqlSession [" + session + "]");
      }
      holder.released();
      // <2.2> 如果不相等，说明不在 Spring 托管的事务中，直接关闭 SqlSession 对象
    } else {
      if (LOGGER.isDebugEnabled()) {
        LOGGER.debug("Closing non transactional SqlSession [" + session + "]");
      }
      session.close();
    }
  }

  /**
   * Returns if the {@code SqlSession} passed as an argument is being managed by Spring
   *
   * @param session a MyBatis SqlSession to check
   * @param sessionFactory the SqlSessionFactory which the SqlSession was built with
   * @return true if session is transactional, otherwise false
   */
  public static boolean isSqlSessionTransactional(SqlSession session, SqlSessionFactory sessionFactory) {
    //判断传入的 SqlSession 参数，是否在 Spring 事务中
    notNull(session, NO_SQL_SESSION_SPECIFIED);
    notNull(sessionFactory, NO_SQL_SESSION_FACTORY_SPECIFIED);
    // 从 TransactionSynchronizationManager 中，获得 SqlSessionHolder 对象
    SqlSessionHolder holder = (SqlSessionHolder) TransactionSynchronizationManager.getResource(sessionFactory);
    // 如果相等，说明在 Spring 托管的事务中
    return (holder != null) && (holder.getSqlSession() == session);
  }

  /**
   * SqlSessionSynchronization ，是 SqlSessionUtils 的内部类，
   * 继承 TransactionSynchronizationAdapter 抽象类，SqlSession 的 同步器，基于 Spring Transaction 体系
   * Callback for cleaning up resources. It cleans TransactionSynchronizationManager and
   * also commits and closes the {@code SqlSession}.
   * It assumes that {@code Connection} life cycle will be managed by
   * {@code DataSourceTransactionManager} or {@code JtaTransactionManager}
   */
  private static final class SqlSessionSynchronization extends TransactionSynchronizationAdapter {
    /**
     * SqlSessionHolder 对象
     */
    private final SqlSessionHolder holder;
    /**
     * SqlSessionFactory 对象
     */
    private final SqlSessionFactory sessionFactory;
    /**
     * 是否开启
     */
    private boolean holderActive = true;

    public SqlSessionSynchronization(SqlSessionHolder holder, SqlSessionFactory sessionFactory) {
      notNull(holder, "Parameter 'holder' must be not null");
      notNull(sessionFactory, "Parameter 'sessionFactory' must be not null");

      this.holder = holder;
      this.sessionFactory = sessionFactory;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getOrder() {
      // order right before any Connection synchronization
      return DataSourceUtils.CONNECTION_SYNCHRONIZATION_ORDER - 1;
    }

    /**
     * #suspend() 方法，当事务挂起时，取消当前线程的绑定的 SqlSessionHolder 对象
     * {@inheritDoc}
     */
    @Override
    public void suspend() {
      if (this.holderActive) {
        if (LOGGER.isDebugEnabled()) {
          LOGGER.debug("Transaction synchronization suspending SqlSession [" + this.holder.getSqlSession() + "]");
        }
        TransactionSynchronizationManager.unbindResource(this.sessionFactory);
      }
    }

    /**
     * #resume() 方法，当事务恢复时，重新绑定当前线程的 SqlSessionHolder 对象
     * 因为，当前 SqlSessionSynchronization 对象中，有 holder 对象，所以可以直接恢复
     * {@inheritDoc}
     */
    @Override
    public void resume() {
      if (this.holderActive) {
        if (LOGGER.isDebugEnabled()) {
          LOGGER.debug("Transaction synchronization resuming SqlSession [" + this.holder.getSqlSession() + "]");
        }
        TransactionSynchronizationManager.bindResource(this.sessionFactory, this.holder);
      }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void beforeCommit(boolean readOnly) {
      // Connection commit or rollback will be handled by ConnectionSynchronization or
      // DataSourceTransactionManager.
      // But, do cleanup the SqlSession / Executor, including flushing BATCH statements so
      // they are actually executed.
      // SpringManagedTransaction will no-op the commit over the jdbc connection
      // TODO This updates 2nd level caches but the tx may be rolledback later on!
      if (TransactionSynchronizationManager.isActualTransactionActive()) {
        try {
          if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Transaction synchronization committing SqlSession [" + this.holder.getSqlSession() + "]");
          }
          this.holder.getSqlSession().commit();
        } catch (PersistenceException p) {
          if (this.holder.getPersistenceExceptionTranslator() != null) {
            DataAccessException translated = this.holder
                .getPersistenceExceptionTranslator()
                .translateExceptionIfPossible(p);
            if (translated != null) {
              throw translated;
            }
          }
          throw p;
        }
      }
    }

    /**
     * 因为，beforeCompletion 方法是在 beforeCommit 之后执行，并且在 beforeCommit 已经提交了事务，所以此处可以放心关闭 SqlSession 对象了
     * {@inheritDoc}
     */
    @Override
    public void beforeCompletion() {
      System.out.println("【beforeCompletion】");
      // Issue #18 Close SqlSession and deregister it now
      // because afterCompletion may be called from a different thread
      if (!this.holder.isOpen()) {
        if (LOGGER.isDebugEnabled()) {
          LOGGER.debug("Transaction synchronization deregistering SqlSession [" + this.holder.getSqlSession() + "]");
        }
        // 取消当前线程的绑定的 SqlSessionHolder 对象
        TransactionSynchronizationManager.unbindResource(sessionFactory);
        // 标记无效
        this.holderActive = false;
        if (LOGGER.isDebugEnabled()) {
          LOGGER.debug("Transaction synchronization closing SqlSession [" + this.holder.getSqlSession() + "]");
        }
        // 标记无效
        this.holder.getSqlSession().close();
      }
    }

    /**
     * 解决可能出现的跨线程的情况
     * {@inheritDoc}
     */
    @Override
    public void afterCompletion(int status) {
      if (this.holderActive) { // 处于有效状态
        // afterCompletion may have been called from a different thread
        // so avoid failing if there is nothing in this one
        if (LOGGER.isDebugEnabled()) {
          LOGGER.debug("Transaction synchronization deregistering SqlSession [" + this.holder.getSqlSession() + "]");
        }
        // 取消当前线程的绑定的 SqlSessionHolder 对象
        TransactionSynchronizationManager.unbindResourceIfPossible(sessionFactory);
        // 标记无效
        this.holderActive = false;
        if (LOGGER.isDebugEnabled()) {
          LOGGER.debug("Transaction synchronization closing SqlSession [" + this.holder.getSqlSession() + "]");
        }
        // 关闭 SqlSession 对象
        this.holder.getSqlSession().close();
      }
      this.holder.reset();
    }
  }

}
