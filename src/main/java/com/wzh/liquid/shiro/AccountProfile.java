package com.wzh.liquid.shiro;

import lombok.Data;

import java.io.Serializable;

/**
 * @author WZH
 * @Description 登录成功返回用户信息的载体AccountProfile
 * @date 2021/12/25 - 21:08
 **/
@Data
public class AccountProfile implements Serializable {
    private Long id;
    private String username;
    private String avatar;
    private String email;
}
