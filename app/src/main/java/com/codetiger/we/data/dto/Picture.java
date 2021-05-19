package com.codetiger.we.data.dto;

public class Picture {
    private int code;
    private String imgurl;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    @Override
    public String toString() {
        return "Picture{" +
                "code=" + code +
                ", imgurl='" + imgurl + '\'' +
                '}';
    }
}
