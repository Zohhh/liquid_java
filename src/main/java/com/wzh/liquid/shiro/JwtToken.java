package com.wzh.liquid.shiro;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * @author WZH
 * @Description shiro默认supports支持的是UsernamePasswordToken，而我们采用jwt的方式，故需要定义一个JwtToken来重写该token
 * @date 2021/12/25 - 20:36
 **/
public class JwtToken implements AuthenticationToken {
    private String token;
    public JwtToken(String token){
        this.token=token;
    }
    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
