package top.otsuland.market.common;

import lombok.Data;

/**
 * 统一响应结果
 * ok
 */
@Data
public class Result<T> {

    private int code;
    private String msg;
    private T data;

    private Result(T data) {
        this.code = 1;
        this.msg = "success";
        this.data = data;
    }

    private Result(int code, String msg) {
        this.code = code;
        this.msg = msg;
        this.data = null;
    }

    private Result(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    /**
     * 冷静版响应，需要错误信息
     * @param <T>
     * @param msg
     * @return
     */
    public static <T> Result<T> error(String msg) {
        return new Result<>(0, msg);
    }

    /**
     * 暴躁版响应，无需任何信息，失败了就是失败了
     * @param <T>
     * @return
     */
    public static <T> Result<T> fail() {
        return new Result<>(0, "failed");
    }

    /**
     * 成功响应，传入数据
     * @param <T>
     * @param data
     * @return
     */
    public static <T> Result<T> success(T data) {
        return new Result<>(data);
    }

    public static <T> Result<T> set(int code, String msg) {
        return new Result<>(code, msg);
    }

    public static <T> Result<T> set(int code, String msg, T data) {
        return new Result<>(code, msg, data);
    }

}
