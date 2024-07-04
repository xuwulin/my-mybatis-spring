package com.xwl.mybatisspring;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(MapperScannerRegistrar.class)
public @interface MapperScan {
    /**
     * 指定扫描的包路径
     *
     * @return
     */
    String value() default "";
}
