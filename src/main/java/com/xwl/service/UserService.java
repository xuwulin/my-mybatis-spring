package com.xwl.service;

import com.xwl.service.mapper.OrderMapper;
import com.xwl.service.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private OrderMapper orderMapper;


    public void test() {
        System.out.println(userMapper.getUser());
        System.out.println(orderMapper.getOrder());
    }
}
