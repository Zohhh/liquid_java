package com.wzh.liquid.common.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author WZH
 * @Description
 * @date 2021/12/29 - 9:41
 **/
@Data
public class LoginDto implements Serializable {
   @NotBlank(message = "昵称不能为空")
   private String username;
   @NotBlank(message = "昵称不能为空")
   private String password;
}
