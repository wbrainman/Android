package com.example.bo.todolist;

import org.litepal.crud.LitePalSupport;

public class TodoDatabase extends LitePalSupport {
    private int id;
    private String item;

    public int getId() {
        return  id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getItem() {
        return  item;
    }

    public void setItem(String item) {
        this.item = item;
    }
}
