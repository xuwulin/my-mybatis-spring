package com.xwl.service;

import com.xwl.mybatisspring.MapperFactoryBean;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Map;

public class Test {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
        UserService userService = (UserService) applicationContext.getBean("userService");
        userService.test();
//        Map<String, MapperFactoryBean> beansOfType = applicationContext.getBeansOfType(MapperFactoryBean.class);
//        for (Map.Entry<String, MapperFactoryBean> entry : beansOfType.entrySet()) {
//            System.out.println(entry);
//        }
    }
}
