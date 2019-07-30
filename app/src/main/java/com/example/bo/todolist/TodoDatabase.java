package com.example.bo.todolist;

import org.litepal.crud.LitePalSupport;

public class TodoDatabase extends LitePalSupport {
    private int index;
    private String item;

    public int getIndex() {
        return  index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getItem() {
        return  item;
    }

    public void setItem(String item) {
        this.item = item;
    }
}
