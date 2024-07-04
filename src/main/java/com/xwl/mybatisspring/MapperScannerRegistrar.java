package com.xwl.mybatisspring;

import com.xwl.service.mapper.OrderMapper;
import com.xwl.service.mapper.UserMapper;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.util.MultiValueMap;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class MapperScannerRegistrar implements ImportBeanDefinitionRegistrar {
    /**
     * @param importingClassMetadata
     * @param registry                就是applicationContext，AnnotationConfigApplicationContext的实现接口
     * @param importBeanNameGenerator
     */
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry, BeanNameGenerator importBeanNameGenerator) {

        Map<String, Object> annotationAttributes = importingClassMetadata.getAnnotationAttributes(MapperScan.class.getName());
        String path = (String) annotationAttributes.get("value");

        // 扫描器，Spring的扫描器是用于扫描bean的，spring扫描到接口是会忽略掉的，但mybatis需要扫描接口
        ClassPathMapperScanner scanner = new ClassPathMapperScanner(registry);
        scanner.addIncludeFilter(new TypeFilter() {
            @Override
            public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
                return true;
            }
        });
        scanner.scan(path);
    }
}
