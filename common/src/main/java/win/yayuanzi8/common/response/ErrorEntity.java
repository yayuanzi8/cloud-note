package win.yayuanzi8.common.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import win.yayuanzi8.common.util.Jacksons;
import win.yayuanzi8.common.util.RestStatus;

/**
 * @author Zhao Junjian
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler", "fieldHandler"}, ignoreUnknown = true)
public class ErrorEntity implements Response {
    private static final long serialVersionUID = 3550224421750657701L;

    /**
     * [M] 平台状态码
     */
    @JsonProperty("code")
    private int code;

    /**
     * [M] 错误信息
     */
    @JsonProperty("msg")
    private String msg;

    /**
     * [C] 详细错误信息
     */
    @JsonProperty("details")
    private Object details;

    public ErrorEntity(RestStatus statusCodes) {
        this(statusCodes, null);
    }

    public ErrorEntity(RestStatus statusCodes, Object details) {
        this.code = statusCodes.code();
        this.msg = statusCodes.message();
        if (details != null) {
            this.details = details;
        }
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getDetails() {
        return details;
    }

    public void setDetails(Object details) {
        this.details = details;
    }

    @Override
    public String toString() {
        return "{" +
                "code: " + code +
                ", msg: '" + msg + '\'' +
                ", details: " + Jacksons.parse(details) +
                '}';
    }
}
