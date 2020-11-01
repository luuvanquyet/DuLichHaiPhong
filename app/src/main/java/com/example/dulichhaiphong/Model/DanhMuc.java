package com.example.dulichhaiphong.Model;

import java.io.Serializable;

public class DanhMuc implements Serializable {
    private Integer id;
    private String tenDanhMuc;

    public DanhMuc(Integer id, String tenDanhMuc) {
        this.id = id;
        this.tenDanhMuc = tenDanhMuc;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTenDanhMuc() {
        return tenDanhMuc;
    }

    public void setTenDanhMuc(String tenDanhMuc) {
        this.tenDanhMuc = tenDanhMuc;
    }
    public String toString(){
        return tenDanhMuc;
    }
}
