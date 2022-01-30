package com.wzh.liquid.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wzh.liquid.entity.User;
import com.wzh.liquid.mapper.UserMapper;
import com.wzh.liquid.service.UserService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author WZH
 * @since 2021-12-24
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
