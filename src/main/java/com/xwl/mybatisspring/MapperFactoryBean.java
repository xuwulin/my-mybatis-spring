package com.xwl.mybatisspring;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;


public class MapperFactoryBean implements FactoryBean {
    private SqlSession sqlSession;

    /**
     * 具体的mapper接口
     */
    private Class mapperInterface;

    /**
     * 通过构造函数给mapperInterface赋值
     * Spring会先调用此构造方法生成MapperFactoryBean对应的bean，然后才会调用getObject方法生成mapper接口的代理对象bean，并加入容器
     * 那Spring会怎么样调用此构造方法呢？因为Spring容器中没有类型为Class的bean
     * 此时就需要用到beanDefinition来解决这个问题
     *
     * @param mapperInterface
     */
    public MapperFactoryBean(Class mapperInterface) {
        this.mapperInterface = mapperInterface;
    }

    @Autowired
    public void setSqlSession(SqlSessionFactory sqlSessionFactory) {
        sqlSessionFactory.getConfiguration().addMapper(mapperInterface);
        this.sqlSession = sqlSessionFactory.openSession();
    }

    @Override
    public Object getObject() throws Exception {
        return sqlSession.getMapper(mapperInterface);
    }

    @Override
    public Class<?> getObjectType() {
        return mapperInterface;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
