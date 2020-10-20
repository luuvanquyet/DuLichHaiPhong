package com.example.dulichhaiphong.Model;

import java.io.Serializable;

public class Anh_Slider_URL implements Serializable {
    private String fileAnh;

    public Anh_Slider_URL(String fileAnh) {
        this.fileAnh = fileAnh;
    }

    public String getFileAnh() {
        return fileAnh;
    }

    public void setFileAnh(String fileAnh) {
        this.fileAnh = fileAnh;
    }
}
