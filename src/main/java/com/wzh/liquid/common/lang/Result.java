package com.wzh.liquid.common.lang;

import lombok.Data;

import java.io.Serializable;

/**
 * @author WZH
 * @Description 后端返回数据给前端时最好有一个统一的格式，
 *              根据返回结果，前端将数据弹窗给用户
 * @date 2021/12/24 - 15:05
 **/
@Data
public class Result implements Serializable {
    private int code;
    private String msg;
    private Object data;

    public static Result succ(Object data) {
        return succ(200, "操作成功", data);
    }

    public static Result succ(int code, String msg, Object data) {
        Result r = new Result();
        r.setCode(code);
        r.setMsg(msg);
        r.setData(data);
        return r;
    }

    public static Result fail(String msg) {
        return fail(400, msg, null);
    }

    public static Result fail(String msg, Object data) {
        return fail(400, msg, data);
    }

    public static Result fail(int code, String msg, Object data) {
        Result r = new Result();
        r.setCode(code);
        r.setMsg(msg);
        r.setData(data);
        return r;
    }

}
