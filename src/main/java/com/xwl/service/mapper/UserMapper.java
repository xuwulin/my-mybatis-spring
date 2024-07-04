package com.xwl.service.mapper;

import org.apache.ibatis.annotations.Select;

public interface UserMapper {

    @Select("select username from user where id = 1")
    String getUser();
}
