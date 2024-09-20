package com.HospitalSystem.Entity;

import lombok.*;
import org.springframework.stereotype.Component;


public class Page {

    private int start;
    private int size;
    private int current_page;

    public Page() {}
    public Page(int start, int size, int current_page) {
        this.start = start;
        this.size = size;
        this.current_page = current_page;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getStart() {
        return start;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }

    public void setCurrent_page(int current_page) {
        this.current_page = current_page;
    }

    public int getCurrent_page() {
        return current_page;
    }
}
