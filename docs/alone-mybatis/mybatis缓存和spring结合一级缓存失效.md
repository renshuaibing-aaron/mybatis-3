1. 缓存相关知识

mybatis   在操作的时候 先找二级缓存 再找一级缓存

一级缓存 
二级缓存

为什么一级缓  4. 映射接口: 在接口中会要执行的Sql用一个方法来表示，具体的Sql写在映射文件中。
        5. 映射文件: 可以理解为是Mybatis编写Sql的地方，通常来说每一张单表都会对应着一个映射文件，在该文件中会定义Sql语句入参和出参的形式。
      
存和二级缓存互斥
为什么

1.1. SqlSession : 代表和数据库的一次会话，向用户提供了操作数据库的方法。
  2. MappedStatement: 代表要发往数据库执行的指令，可以理解为是Sql的抽象表示。
  3. Executor: 具体用来和数据库交互的执行器，接受MappedStatement作为参数。
总结
1、Mybatis一级缓存的生命周期和SqlSession一致。 
2、Mybatis的缓存没有更新缓存和缓存过期的概念，同时只是使用了默认的hashmap，也没有做容量上的限定。 
3、Mybatis的一级缓存最大范围是SqlSession内部，有多个SqlSession或者分布式的环境下，有操作数据库写的话，会引起脏数据，建议是把一级缓存的默认级别设定为Statement，即不使用一级缓存。


mybatis-spring 一级缓存

大概的意思是说：mybatis-spring中的sqlsession通过spring去管理，

前面说到mybatis的一级缓存生效的范围是sqlsession，是为了在sqlsession没有关闭时，业务需要重复查询相同数据使用的。一旦sqlsession关闭，则由这个sqlsession缓存的数据将会被清空。

spring对mybatis的sqlsession的使用是由template控制的，sqlsession又被spring当作resource放在当前线程的上下文里（threadlocal),spring通过mybatis调用数据库的过程如下：

1、需要访问数据
2、spring检查到了这种需求，于是去申请一个mybatis的sqlsession，并将申请到的sqlsession与当前线程绑定，放入threadlocal里面
3、template从threadlocal获取到sqlsession，去执行查询
4、查询结束，清空threadlocal中与当前线程绑定的sqlsession，释放资源
5、又需要访问数据
6、返回到步骤2

 这里说明缓存失效了！！！ 两次执行的sqlsession都不一样 缓存怎么会不失效
 
 springmybatis的 组件是用的动态代理来保证线程的安全性  其实在mybatis中也有一个保证线程安全性的工具SqlSessionManager  这个采用的不是动态代理 
 为什么采用动态代理来实现呢？？

7.说明要明白mybatis的生命周期 各个组件的生命周期


