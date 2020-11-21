package com.example.dulichhaiphong.Model;

import java.io.Serializable;
import java.util.ArrayList;

public class Baiviet implements Serializable {
    private String idBaiViet;
    private String tenBaiViet;
    private String tomTat;
    private String soLike;
    private String ngayDang;
    private String anhDaidien;
    private String noiDung;
    private String code;
    private ArrayList<AnhLienQuan> anhLienQuanList;

    public Baiviet(String idBaiViet, String tenBaiViet, String tomTat, String soLike, String ngayDang, String anhDaidien,String noiDung,String code) {
        this.idBaiViet = idBaiViet;
        this.tenBaiViet = tenBaiViet;
        this.tomTat = tomTat;
        this.soLike = soLike;
        this.ngayDang = ngayDang;
        this.anhDaidien = anhDaidien;
        this.noiDung = noiDung;
        this.code = code;
    }

    public Baiviet(String idBaiViet, String tenBaiViet, String tomTat, String soLike, String ngayDang, String anhDaidien, String noiDung, ArrayList<AnhLienQuan> anhLienQuanList) {
        this.idBaiViet = idBaiViet;
        this.tenBaiViet = tenBaiViet;
        this.tomTat = tomTat;
        this.soLike = soLike;
        this.ngayDang = ngayDang;
        this.anhDaidien = anhDaidien;
        this.noiDung = noiDung;
        this.anhLienQuanList = anhLienQuanList;
    }

    public String getIdBaiViet() {
        return idBaiViet;
    }

    public void setIdBaiViet(String idBaiViet) {
        this.idBaiViet = idBaiViet;
    }

    public String getTenBaiViet() {
        return tenBaiViet;
    }

    public void setTenBaiViet(String tenBaiViet) {
        this.tenBaiViet = tenBaiViet;
    }

    public String getTomTat() {
        return tomTat;
    }

    public void setTomTat(String tomTat) {
        this.tomTat = tomTat;
    }

    public String getSoLike() {
        return soLike;
    }

    public void setSoLike(String soLike) {
        this.soLike = soLike;
    }

    public String getNgayDang() {
        return ngayDang;
    }

    public void setNgayDang(String ngayDang) {
        this.ngayDang = ngayDang;
    }

    public String getAnhDaidien() {
        return anhDaidien;
    }

    public void setAnhDaidien(String anhDaidien) {
        this.anhDaidien = anhDaidien;
    }

    public String getNoiDung() {
        return noiDung;
    }

    public void setNoiDung(String noiDung) {
        this.noiDung = noiDung;
    }

    public ArrayList<AnhLienQuan> getAnhLienQuanList() {
        return anhLienQuanList;
    }

    public void setAnhLienQuanList(ArrayList<AnhLienQuan> anhLienQuanList) {
        this.anhLienQuanList = anhLienQuanList;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
