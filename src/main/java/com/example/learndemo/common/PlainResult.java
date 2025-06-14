package com.example.learndemo.common;

import lombok.Data;

@Data
public class PlainResult<T> {
    private Integer code;
    private String message;
    private T data;

    public static <T> PlainResult<T> success(T data) {
        PlainResult<T> result = new PlainResult<>();
        result.setCode(200);
        result.setMessage("success");
        result.setData(data);
        return result;
    }

    public static <T> PlainResult<T> success(String message, T data) {
        PlainResult<T> result = new PlainResult<>();
        result.setCode(200);
        result.setMessage(message);
        result.setData(data);
        return result;
    }

    public static <T> PlainResult<T> error(Integer code, String message) {
        PlainResult<T> result = new PlainResult<>();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }
}
