package com.wzh.liquid.controller;


import com.wzh.liquid.common.lang.Result;
import com.wzh.liquid.entity.User;
import com.wzh.liquid.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author WZH
 * @since 2021-12-24
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;
    @GetMapping("/{id}")
    public Result test(@PathVariable("id") Long id){
        User user = userService.getById(id);
        return Result.succ(200,"操作成功",user);
    }
    @PostMapping("/save")
    public Object testUser(@Validated @RequestBody User user){
        return user.toString();
    }

}
