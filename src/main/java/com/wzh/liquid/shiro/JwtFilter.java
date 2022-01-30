package com.wzh.liquid.shiro;

import cn.hutool.json.JSONUtil;

import com.wzh.liquid.common.lang.Result;
import com.wzh.liquid.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExpiredCredentialsException;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author WZH
 * @Description 自定义过滤器
 * @date 2021/12/25 - 20:48
 **/
@Component
public class JwtFilter extends AuthenticatingFilter {
    @Autowired
    JwtUtils jwtUtils;

    /**
     * 实现登录，生成自定义的JwtToken
     * @param servletRequest
     * @param servletResponse
     * @return
     * @throws Exception
     */
    @Override
    protected AuthenticationToken createToken(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        //获取token
        HttpServletRequest request= (HttpServletRequest) servletRequest;
        String jwt = request.getHeader("Authorization");
        if (StringUtils.isEmpty(jwt)){
            return null;
        }

        return new JwtToken(jwt);
    }

    /**
     * 拦截校验
     * @description 当头部没有Authorizaation,直接通过，不需要自动登录
     *              当有Authorization时，需要进行jwt校验
     * @param servletRequest
     * @param servletResponse
     * @return
     * @throws Exception
     */
    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        HttpServletRequest request= (HttpServletRequest) servletRequest;
        String token = request.getHeader("Authorization");
        if (StringUtils.isEmpty(token)){
            return true;
        }else {
            //判断是否过期
            Claims claim= jwtUtils.getClaimByToken(token);
            if (claim==null || jwtUtils.isTokenExpire(claim.getExpiration())){
                throw new ExpiredCredentialsException("token已失效，请重新登录");
            }
        }
        //执行自动登录
        return executeLogin(servletRequest,servletResponse);
    }

    /**
     * 执行登录出现异常
     * @param token
     * @param e
     * @param request
     * @param response
     * @return
     */
    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
        HttpServletResponse httpResponse=(HttpServletResponse) response;
        try {
            //处理登录失败异常
            Throwable throwable =e.getCause()==null? e: e.getCause();
            Result r= Result.fail(throwable.getMessage());
            String json = JSONUtil.toJsonStr(r);
            httpResponse.getWriter().print(json);
        }catch (IOException e1){

        }
        return false;
    }

    /**
     * 对跨域进行处理
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
        HttpServletResponse httpServletResponse = WebUtils.toHttp(response);
        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
        // 跨域时会首先发送一个OPTIONS请求，这里我们给OPTIONS请求直接返回正常状态
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(org.springframework.http.HttpStatus.OK.value());
            return false;
        }
        return super.preHandle(request, response);
    }
}
