package com.wzh.liquid.utils;

import com.wzh.liquid.shiro.AccountProfile;
import org.apache.shiro.SecurityUtils;

/**
 * @author WZH
 * @Description
 * @date 2021/12/29 - 13:52
 **/
public class ShiroUtil {

    public static AccountProfile getProfile(){
        return (AccountProfile) SecurityUtils.getSubject().getPrincipal();
    }
}
