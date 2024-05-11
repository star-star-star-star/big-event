package com.example.big_event.message;

/**
 * @apiNote 响应类型，对后端的每一个操作进行响应。
 */
public class Result {
    private Boolean code;   //操作状态，true=操作成功，false=操作失败
    private String message; //响应消息
    private Object data; //响应数据

    public Result() {
    }

    public Result(Boolean code, String message) {
        this.code = code;
        this.message = message;
    }

    public Result(Boolean code, Object data) {
        this.code = code;
        this.data = data;
    }

    public Result(Boolean code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public Boolean getCode() {
        return code;
    }

    public void setCode(Boolean code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Result{");
        sb.append("code=").append(code);
        sb.append(", message='").append(message).append('\'');
        sb.append(", data=").append(data);
        sb.append('}');
        return sb.toString();
    }
}
