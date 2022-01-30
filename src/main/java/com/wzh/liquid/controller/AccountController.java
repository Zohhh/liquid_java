package com.wzh.liquid.controller;

import cn.hutool.core.map.MapUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wzh.liquid.common.dto.LoginDto;
import com.wzh.liquid.common.lang.Result;
import com.wzh.liquid.entity.User;
import com.wzh.liquid.service.UserService;
import com.wzh.liquid.utils.JwtUtils;
import io.jsonwebtoken.lang.Assert;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * @author WZH
 * @Description 登录接口
 * @date 2021/12/29 - 9:21
 **/
@RestController
//@RequestMapping("/user")
public class AccountController {

    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    UserService userService;

    /**
     * 默认账号密码  markerhub / 111111
      * @param loginDto
     * @param response
     * @return
     */
    @CrossOrigin
    @PostMapping("/login")
    public Result login(@Validated @RequestBody LoginDto loginDto, HttpServletResponse response){
        User user= userService.getOne(new QueryWrapper<User>().eq("username", loginDto.getUsername()));
        //用户名是否为空
        Assert.notNull(user,"用户不存在");
        if (!user.getPassword().equals(SecureUtil.md5(loginDto.getPassword()))){
            return Result.fail("密码错误");
        }
        String jwt = jwtUtils.generateToken(user.getId());
        response.setHeader("Authorization",jwt);
        response.setHeader("Access-Control-Expose-Headers","Authorization");

        return Result.succ(MapUtil.builder()
                .put("id",user.getId())
                .put("username",user.getUsername())
                .put("avatar",user.getAvatar())
                .put("email",user.getEmail())
                .map()
        );

    }

    /**
     * 退出登录
     * @return
     */
    @GetMapping("/logout")
    @RequiresAuthentication
    public Result logout(){
        //需开启redis
        SecurityUtils.getSubject().logout();
        return Result.succ(null);
    }
}
