package com.codetiger.we.data.result;

import com.codetiger.we.data.dto.Music;

public class MusicResult {
    private String code;
    private Music data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Music getData() {
        return data;
    }

    public void setData(Music data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "MusicResult{" +
                "code='" + code + '\'' +
                ", data=" + data +
                '}';
    }
}
