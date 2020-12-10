1.提起事的四大特性 四大隔离接 七大传播特性

关于mybatis事务的封装

2.
以前使用JDBC的时候，如果要开启事务，我们需要调用conn.setAutoCommit(false)方法来关闭自动提交，之后才能进行事务操作，
否则每一次对数据库的操作都会持久化到磁盘中。
而mybatis呢，如果底层使用JDBC（在mybatis.xml中配置的transactionManager标签的type设为jdbc的话），
那么，mybatis会默认开启事务，也就是说，mybatis默认是关闭自动提交的。
在mybatis中，如果我们执行了数据库的修改操作（insert、update、delete），
必须调用session.commit()方法，所做的修改才能持久化到磁盘。