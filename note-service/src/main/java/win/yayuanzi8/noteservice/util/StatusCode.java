package win.yayuanzi8.noteservice.util;

import com.google.common.collect.ImmutableMap;
import win.yayuanzi8.common.util.RestStatus;

public enum StatusCode implements RestStatus {
    OK(20000, "请求成功"),

    // 40xxx 客户端不合法的请求
    INVALID_MODEL_FIELDS(40001, "字段校验非法"),

    /**
     * 参数类型非法，常见于SpringMVC中String无法找到对应的enum而抛出的异常
     */
    INVALID_PARAMS_CONVERSION(40002, "参数类型非法"),

    // 41xxx 请求方式出错
    /**
     * http media type not supported
     */
    HTTP_MESSAGE_NOT_READABLE(41001, "HTTP消息不可读"),

    /**
     * 请求方式非法
     */
    REQUEST_METHOD_NOT_SUPPORTED(41002, "不支持的HTTP请求方法"),

    // 成功接收请求, 但是处理失败
    /**
     * Duplicate Key
     */
    DUPLICATE_KEY(42001, "操作过快, 请稍后再试"),

    AUTH_FAIL(42011, "认证失败"),

    FETCH_INFO_FAIL(42012, "拉取用户信息失败"),

    MOVE_POS_ERROR(42013, "移动目录不正确"),

    HAS_SHARE(42014, "该笔记已分享过"),

    SHARE_NOT_EXISTS(42015, "该笔记已被取消分享"),

    DUPLICATE_DIR_NAME(42016, "同一目录已有同名文件夹"),

    DUPLICATE_NOTE_NAME(42017, "同一目录已有同名笔记"),

    COPY_POS_ERROR(42018, "不能将目录复制到自身或子目录"),

    DIR_NOT_EXIST(42019, "找不到目录"),

    SAME_AS_OLD_DIR_NAME(42020, "旧目录与新目录同名"),

    RENAME_ERROR(42021, "重命名失败"),

    NOTE_NOT_EXIST(42022, "找不到笔记"),

    SAME_AS_OLD_NOTE_NAME(42023, "旧笔记与新笔记同名"),
    // 50xxx 服务端异常
    /**
     * 用于处理未知的服务端错误
     */
    SERVER_UNKNOWN_ERROR(50001, "服务端异常, 请稍后再试");

    private final int code;

    private final String message;

    private static final ImmutableMap<Integer, StatusCode> CACHE;

    static {
        final ImmutableMap.Builder<Integer, StatusCode> builder = ImmutableMap.builder();
        for (StatusCode statusCode : values()) {
            builder.put(statusCode.code(), statusCode);
        }
        CACHE = builder.build();
    }

    StatusCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public static StatusCode valueOfCode(int code) {
        final StatusCode status = CACHE.get(code);
        if (status == null) {
            throw new IllegalArgumentException("No matching constant for [" + code + "]");
        }
        return status;
    }

    @Override
    public int code() {
        return code;
    }

    @Override
    public String message() {
        return message;
    }
}
