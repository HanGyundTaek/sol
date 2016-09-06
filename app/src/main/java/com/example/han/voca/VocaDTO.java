package com.example.han.voca;

import java.io.Serializable;

/**
 * Created by Han on 2016-09-06.
 */
public class VocaDTO implements Serializable {
    String voca;
    String date;

    public String getVoca() {
        return voca;
    }

    public void setVoca(String voca) {
        this.voca = voca;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
