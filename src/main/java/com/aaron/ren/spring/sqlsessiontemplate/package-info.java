package com.aaron.ren.spring.sqlsessiontemplate;


/*  @MapperScan扫描指定的包，对每个Mapper，以它的名字注册了实际类型是MapperFactoryBean的Bean定义。
*
*
* mybatis整合Spring  解决俩个问题
*
*
*   SqlSessionFactory，SqlSession 如何生成?
    Mapper 代理如何生成？如何运行？
*显而易见的一个东西FactoryBean  只要实现了这个接口的对象 在容器里面获取的就不是 其自身 而是这个类的getobject()返回值
* MapperScan 扫描的作用是把Mapper 接口转化为MapperFactoryBean  这个的作用 明显可以看出就是
* 获取getSqlSession().getMapper(this.mapperInterface)
*
* 这篇文章讲的不错
* https://www.jianshu.com/p/c2b2d6f90ba5
*
* https://www.jianshu.com/p/3289b080e6da
*
* https://www.jianshu.com/p/e500eb7c9040
*
* 还有一篇 讲的也可以 讲明白了二级缓存的原因 以及spring
* 为什么一级缓存为什么失效
*
*
*
*
*
*
*
*
*
*
*
*
*
*
*
*
*
*
* */
