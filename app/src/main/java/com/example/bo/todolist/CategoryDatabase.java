package com.example.bo.todolist;

import org.litepal.crud.LitePalSupport;

public class CategoryDatabase extends LitePalSupport {
    private int id;
    private String item;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getItem() {
        return  this.item;
    }


}
