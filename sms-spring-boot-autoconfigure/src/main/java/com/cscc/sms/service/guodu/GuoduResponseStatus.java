package com.cscc.sms.service.guodu;

/**
 * @Description: 状态报告错误码
 * @Author ajoe.Liu
 * @Date 2019/12/18 16:53
 */
public enum  GuoduResponseStatus {
    /**
     * 状态报告错误码
     */
    BATCH_SUCCESS("01","批量短信提交成功"),
    IP_VERIFY_FAIL("02","IP验证失败"),
    SINGLE_SUCCESS("03","单挑短信提交成功"),
    USER_ERROR("04","用户名错误"),
    PASSWORD_ERROR("05","密码错误"),
    PARAM_ERROR("06","参数有误"),
    TIME_FORMAT_ERROR("07","SendTime格式错误"),
    CONTENT_EMPTY("08","短信内容为空"),
    MOBILE_EMPTY("09","手机号码为空"),
    PARAM_FORMAT_ERROR("10","AppendID格式错误"),
    SUBMIT_EXCEPTION("-1","提交异常"),
    SEND_SUCCESS("0","发送成功")
    ;

    private String value;
    private String msg;
    GuoduResponseStatus(String value, String msg) {
        this.value = value;
        this.msg = msg;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static GuoduResponseStatus of(String value){
        GuoduResponseStatus[] values = GuoduResponseStatus.values();
        for (GuoduResponseStatus status:values){
            if(value.equals(status.value)){
                return status;
            }
        }
        return  null;
    }

    public static String toMsg(String value){
        GuoduResponseStatus status = of(value);
        return  status==null?"no status":status.getMsg();
    }
}
