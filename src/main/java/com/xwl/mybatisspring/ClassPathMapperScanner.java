package com.xwl.mybatisspring;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
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
            BeanDefinition beanDefinition = beanDefinitionHolder.getBeanDefinition();
            // 指定MapperFactoryBean构造函数的参数
            beanDefinition.getConstructorArgumentValues().addGenericArgumentValue(beanDefinition.getBeanClassName());
            // 修改beanDefinition的类型为MapperFactoryBean
            beanDefinition.setBeanClassName(MapperFactoryBean.class.getName());
        }

        return beanDefinitionHolders;
    }
}
