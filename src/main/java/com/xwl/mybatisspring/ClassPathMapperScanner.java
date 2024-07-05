package com.xwl.mybatisspring;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.classreading.MetadataReader;

import java.io.IOException;
import java.util.Set;

/**
 * 自定义扫描器
 */
public class ClassPathMapperScanner extends ClassPathBeanDefinitionScanner {
    /**
     * 构造方法
     *
     * @param registry 注册器（Spring容器）
     */
    public ClassPathMapperScanner(BeanDefinitionRegistry registry) {
        super(registry);
    }

    /**
     * 用于判断扫描到的类上是否有@Component注解，有的话，则将其加入到beanDefinition中
     * 但是这里是扫描mapper接口，有没有@Component注解都无所谓，直接返回true，也可以不用重写这个方法
     *
     * @param metadataReader the ASM ClassReader for the class
     * @return
     * @throws IOException
     */
    @Override
    protected boolean isCandidateComponent(MetadataReader metadataReader) throws IOException {
        return true;
    }

    /**
     * Spring的扫描器是用于扫描bean的，spring扫描到接口是会忽略掉的，但mybatis需要扫描接口，所以需要重写该方法
     * 当扫描器扫描到接口时，返回true
     *
     * @param beanDefinition
     * @return
     */
    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        return beanDefinition.getMetadata().isInterface();
    }

    /**
     * Spring是不会扫描接口并得到beanDefinition的，所以需要重写该方法
     *
     * @param basePackages 扫描的包路径
     * @return
     */
    @Override
    protected Set<BeanDefinitionHolder> doScan(String... basePackages) {
        Set<BeanDefinitionHolder> beanDefinitionHolders = super.doScan(basePackages);
        // 此时获取的beanDefinition中的类型是mapper接口，不是我们想要的MapperFactoryBean类型，因此需要手动修改
        for (BeanDefinitionHolder beanDefinitionHolder : beanDefinitionHolders) {
            // 获取beanDefinition
            GenericBeanDefinition beanDefinition = (GenericBeanDefinition) beanDefinitionHolder.getBeanDefinition();
            // 指定MapperFactoryBean构造函数的参数
            beanDefinition.getConstructorArgumentValues().addGenericArgumentValue(beanDefinition.getBeanClassName());
            // 修改beanDefinition的类型为MapperFactoryBean
            beanDefinition.setBeanClassName(MapperFactoryBean.class.getName());
            /**
             * 一般的bean，其定义中，autowireMode默认是AUTOWIRE_NO
             *
             * 设置自动装配：按照类型装配，（对于 “构造方法” 和 “工厂方法” 来说选择AUTOWIRE_CONSTRUCTOR）
             * 将BeanDefinition的autowireMode属性改成 AUTOWIRE_BY_TYPE，
             * 后面实例化该bean的时候spring会自动调用类中的所有set方法，也就是说，不管set方法上有没有@Autowired注解，都会调用set方法。
             *
             * mybatis-spring 创建SqlSessionFactoryBean对象时，会调用set方法注入SqlSessionFactory对象（在SqlSessionFactoryBean的父类SqlSessionDaoSupport中申明的setSqlSessionFactory()方法）
             *
             */
            beanDefinition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
        }

        return beanDefinitionHolders;
    }
}
