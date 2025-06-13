package com.example.learndemo.common;

import lombok.Data;

@Data
public class PlainResult<T> {
    private T data;//存储返回数据
    private int code;//存储状态码
    private String msg;//存储返回消息
    private boolean success;//标记操作是否成功

    public PlainResult() {}

    public static <T> PlainResult<T> success(T data) {
        PlainResult<T> plainResult = new PlainResult<>();
        plainResult.setSuccess(true);
        plainResult.setData(data);
        plainResult.setCode(200);
        plainResult.setMsg("success");
        return plainResult;
    }

    public static <T> PlainResult<T> fail(int code, String msg) {
        PlainResult<T> plainResult = new PlainResult<>();
        plainResult.setSuccess(false);
        plainResult.setCode(code);
        plainResult.setMsg(msg);
        return plainResult;
    }


}
