package com.wzh.liquid.shiro;

import cn.hutool.core.bean.BeanUtil;

import com.wzh.liquid.entity.User;
import com.wzh.liquid.service.UserService;
import com.wzh.liquid.utils.JwtUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author WZH
 * @Description shiro进行登录或者权限校验的逻辑
 * @date 2021/12/25 - 20:24
 **/
@Component
public class AccountRealm extends AuthorizingRealm {
    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    UserService userService;
    //让realm支持jwt凭证校验
    @Override
    public boolean supports(AuthenticationToken token){
        return token instanceof com.wzh.liquid.shiro.JwtToken;
    }
    //权限校验
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }
    //登录认证校验
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        //传入的AuthenticationToken强转JwtToken
        JwtToken jwt=(JwtToken) token;
        //获取jwtToken中的userId
        String userId = jwtUtils.getClaimByToken((String) jwt.getPrincipal()).getSubject();
        //查询数据库
        User user = userService.getById(Long.parseLong(userId));
        if(user == null) {
            throw new UnknownAccountException("账户不存在！");
        }
        if(user.getStatus() == -1) {
            throw new LockedAccountException("账户已被锁定！");
        }
        //显示信息放在该载体中，对于密码这种隐私信息不需要放在该载体中
        AccountProfile profile = new AccountProfile();
        BeanUtil.copyProperties(user, profile);
        //将token中用户基本信息返回给shiro
        return new SimpleAuthenticationInfo(profile, jwt.getCredentials(), getName());

    }
}
