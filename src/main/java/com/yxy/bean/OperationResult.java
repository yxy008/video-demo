package com.yxy.bean;

/**
 * @program: demo
 * @description: 返回结果参数
 * @author: yuxinyu
 * @create: 2020-07-01 16:38
 **/
public class OperationResult {

    /**
     * 是否成功
     */
    private boolean success = false;

    /**
     * 消息
     */
    private String message = null;

    /**
     * 结果
     */
    private String result = null;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
