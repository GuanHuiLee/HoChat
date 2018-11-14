package com.zgg.hochat.base;

/**
 * Created by niujingtong on 2018/7/13.
 * 模块：
 */
public class BaseResult<T> {
    private String msg;
    private int code;
    private T result;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}
