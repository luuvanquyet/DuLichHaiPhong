package com.example.dulichhaiphong.Model;

public class FAQ {
    String id;
    String tenCauHoi;
    String cauTraLoi;

    public FAQ(String id, String tenCauHoi, String cauTraLoi) {
        this.id = id;
        this.tenCauHoi = tenCauHoi;
        this.cauTraLoi = cauTraLoi;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTenCauHoi() {
        return tenCauHoi;
    }

    public void setTenCauHoi(String tenCauHoi) {
        this.tenCauHoi = tenCauHoi;
    }

    public String getCauTraLoi() {
        return cauTraLoi;
    }

    public void setCauTraLoi(String cauTraLoi) {
        this.cauTraLoi = cauTraLoi;
    }
}
