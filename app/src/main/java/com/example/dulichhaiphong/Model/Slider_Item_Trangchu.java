package com.example.dulichhaiphong.Model;

public class Slider_Item_Trangchu {
    private int imge;
    private String tenImge;

    public Slider_Item_Trangchu(int imge, String tenImge) {
        this.imge = imge;
        this.tenImge = tenImge;
    }

    public int getImge() {
        return imge;
    }

    public void setImge(int imge) {
        this.imge = imge;
    }

    public String getTenImge() {
        return tenImge;
    }

    public void setTenImge(String tenImge) {
        this.tenImge = tenImge;
    }
}
