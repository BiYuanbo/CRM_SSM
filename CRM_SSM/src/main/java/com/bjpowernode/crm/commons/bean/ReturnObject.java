package com.bjpowernode.crm.commons.bean;

public class ReturnObject {
    private String code;        //处理成功或失败的标记，1---成功，0---失败
    private String message;     //提示信息
    private Object oretData;    //其他数据

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getOretData() {
        return oretData;
    }

    public void setOretData(Object oretData) {
        this.oretData = oretData;
    }

    @Override
    public String toString() {
        return "ReturnObject{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", oretData=" + oretData +
                '}';
    }
}
