1.Mybatis的核心原理 大致步骤：
  创建 SqlSessionFactory 对象。
  通过 SqlSessionFactory 获取 SqlSession 对象。
  通过 SqlSession 获得 Mapper 代理对象。
  通过 Mapper 代理对象，执行数据库操作。
  执行成功，则使用 SqlSession 提交事务。
  执行失败，则使用 SqlSession 回滚事务。
  最终,关闭会话。


1.mybatis的核心组件


SqlSession

Executor

StatementHandler

ParameterHandler

ResultSetHandler

TypeHandler

MappedStatement

Configuration

过程
解析全局文件生成Configuration 
利用解析的configuration对象调用build()方法 生成DefautSqlSessionFactory ----- 建造者模式
利用DefautSqlSessionFactory生成SqlSession 默认是DefaultSqlSession 这中间Executor 也创建了 -------工厂模式
利用生成的sqlsession执行具体的sql语句
    这里sqlsession根据statement_id 在mybatis配置对象Configuration获取到相对应的MappedStatement对象，然后调用执行器来执行具体的操作
    即是执行query()和queryFromDatabase()  方法 ----这里会为当前的查询创建一个缓存key 查找缓存如果缓存没有值 直接从数据库读取 执行查询后进入缓存
       缓存里面没有值执行doQuery()在SimpleExecutor类中重写的方法 ---创建连接对象Statement 创建StatementHandler 执行sql语句
          prepareStatement()和query()
          根据具体传入的参数，动态地生成需要执行的SQL语句，用BoundSql对象表示
          为当前的查询创建一个缓存Key
          缓存中没有值，直接从数据库中读取数据
          执行查询，返回List 结果，然后 将查询的结果放入缓存之中
          根据既有的参数，创建StatementHandler对象来执行查询操作
          将创建Statement传递给StatementHandler对象,调用parameterize()方法赋值
          调用StatementHandler.query()方法，返回List结果集

具体的路径
https://mmbiz.qpic.cn/mmbiz_png/vnOqylzBGCQeJEvNibf2Q5k6OoDEgeRH2NjBf4IW7T8JmiaAkhtWf07icHibqNvUpDJC523MaBWfA2KvlM7ZcU5nmg/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1
