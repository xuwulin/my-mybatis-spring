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

    /**
     * 在EsMapperScanner中，设置了 definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
     * 实例化该bean的时候spring会自动调用类中的所有set方法，也就是说，不管set方法上有没有@Autowired注解，都会调用set方法。
     * 并且set方法所需要的参数，spring也会从容器中去获取并自动注入。
     * <p>
     * 当然，也可以不设置definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
     * 直接在属性或者set方法上使用@Autowired注解也行
     *
     * @param sqlSessionFactory
     */
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
