package org.apache.ibatis.transaction.jdbc;

import java.sql.Connection;

import javax.sql.DataSource;

import org.apache.ibatis.session.TransactionIsolationLevel;
import org.apache.ibatis.transaction.Transaction;
import org.apache.ibatis.transaction.TransactionFactory;

/**
 * Creates {@link JdbcTransaction} instances.
 *
 * @author Clinton Begin
 *
 * @see JdbcTransaction
 */
public class JdbcTransactionFactory implements TransactionFactory {
  /**
   * 根据给定的数据库连接Connection创建Transaction
   * @param conn Existing database connection
   * @return
   */
  @Override
  public Transaction newTransaction(Connection conn) {
    System.out.println("【根据给定的数据库连接Connection创建Transaction】");
    return new JdbcTransaction(conn);
  }
  /**
   * 根据DataSource、隔离级别和是否自动提交创建Transacion
   *
   * @param ds
   * @param level Desired isolation level
   * @param autoCommit Desired autocommit
   * @return
   */
  @Override
  public Transaction newTransaction(DataSource ds, TransactionIsolationLevel level, boolean autoCommit) {
    System.out.println("【根据DataSource、隔离级别和是否自动提交创建Transacion】");
    return new JdbcTransaction(ds, level, autoCommit);
  }
}
