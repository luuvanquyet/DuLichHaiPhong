package com.example.dulichhaiphong.Model;

public class Comment {
    private String idcomment;
    private String tenNguoidung;
    private String filePhoto;
    private String thoiGiandang;
    private String noiDung;

    public Comment(String idcomment, String tenNguoidung, String filePhoto, String thoiGiandang, String noiDung) {
        this.idcomment = idcomment;
        this.tenNguoidung = tenNguoidung;
        this.filePhoto = filePhoto;
        this.thoiGiandang = thoiGiandang;
        this.noiDung = noiDung;
    }

    public String getIdcomment() {
        return idcomment;
    }

    public void setIdcomment(String idcomment) {
        this.idcomment = idcomment;
    }

    public String getTenNguoidung() {
        return tenNguoidung;
    }

    public void setTenNguoidung(String tenNguoidung) {
        this.tenNguoidung = tenNguoidung;
    }

    public String getFilePhoto() {
        return filePhoto;
    }

    public void setFilePhoto(String filePhoto) {
        this.filePhoto = filePhoto;
    }

    public String getThoiGiandang() {
        return thoiGiandang;
    }

    public void setThoiGiandang(String thoiGiandang) {
        this.thoiGiandang = thoiGiandang;
    }

    public String getNoiDung() {
        return noiDung;
    }

    public void setNoiDung(String noiDung) {
        this.noiDung = noiDung;
    }
}
