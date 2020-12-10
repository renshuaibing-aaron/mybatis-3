1.mybatis和spring结合怎么解决sqlsessionTemplate的线程安全问题的？
sqlsession的使用有两种个方式 
DefaultSqlSession  mybatis默认的方式 线程不安全
SqlSessionManager  利用threadlocal解决线程的安全问题
 在spring-mybatis里面  并不是使用threadlocal解决的 而是利用的动态代理机制 在每次执行的时候 获取的sqlsession
 都是从事务里面获取的 和事务绑定的sqlsession
 
 
 
2.spring-mybatis的一级缓存的失效问题？