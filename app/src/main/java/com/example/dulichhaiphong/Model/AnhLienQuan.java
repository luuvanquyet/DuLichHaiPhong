package com.example.dulichhaiphong.Model;

import java.io.Serializable;

public class AnhLienQuan implements Serializable {
    private String id;
    private String title;
    private String file;

    public AnhLienQuan(String id, String title, String file) {
        this.id = id;
        this.title = title;
        this.file = file;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }
}
