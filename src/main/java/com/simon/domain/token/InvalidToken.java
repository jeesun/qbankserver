package com.simon.domain.token;

/**
 * Created by simon on 2017/2/22.
 */

public class InvalidToken {
    private String error;
    private String error_description;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getError_description() {
        return error_description;
    }

    public void setError_description(String error_description) {
        this.error_description = error_description;
    }

    @Override
    public String toString() {
        return "InvalidToken{" +
                "error='" + error + '\'' +
                ", error_description='" + error_description + '\'' +
                '}';
    }

    public boolean isNull(){
        return (null==error)||"".equals(error)||"null".equals(error)
                ||(null==error_description)||"".equals(error_description)||"null".equals(error_description);
    }

    //invalid token大部分情况下都是null，我希望比较一次就立刻返回结果
    public boolean isNotNull(){
        return (null!=error)||!"".equals(error)||!"null".equals(error)
                ||(null!=error_description)||!"".equals(error_description)||!"null".equals(error_description);
    }
}
