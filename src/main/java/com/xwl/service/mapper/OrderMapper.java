package com.xwl.service.mapper;

import org.apache.ibatis.annotations.Select;

public interface OrderMapper {
    @Select("select 'order'")
    String getOrder();
}
