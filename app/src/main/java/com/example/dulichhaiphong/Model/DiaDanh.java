package com.example.dulichhaiphong.Model;

import com.google.android.gms.maps.model.LatLng;

public class DiaDanh {
    private String id;
    private String tenDiadanh;
    private String tomTat;
    private String noidung;
    private LatLng toaDo;
    private String anhDaidien;

    public DiaDanh(String id, String tenDiadanh, String tomTat, String noidung, LatLng toaDo, String anhDaidien) {
        this.id = id;
        this.tenDiadanh = tenDiadanh;
        this.tomTat = tomTat;
        this.noidung = noidung;
        this.toaDo = toaDo;
        this.anhDaidien = anhDaidien;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTenDiadanh() {
        return tenDiadanh;
    }

    public void setTenDiadanh(String tenDiadanh) {
        this.tenDiadanh = tenDiadanh;
    }

    public String getTomTat() {
        return tomTat;
    }

    public void setTomTat(String tomTat) {
        this.tomTat = tomTat;
    }

    public String getNoidung() {
        return noidung;
    }

    public void setNoidung(String noidung) {
        this.noidung = noidung;
    }

    public LatLng getToaDo() {
        return toaDo;
    }

    public void setToaDo(LatLng toaDo) {
        this.toaDo = toaDo;
    }

    public String getAnhDaidien() {
        return anhDaidien;
    }

    public void setAnhDaidien(String anhDaidien) {
        this.anhDaidien = anhDaidien;
    }
}
