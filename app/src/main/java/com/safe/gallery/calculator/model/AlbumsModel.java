package com.safe.gallery.calculator.model;

public class AlbumsModel {

    private int itemCount;
    private int itemIcon;
    private String itemName;

    public AlbumsModel(String itemName, int itemCount, int itemIcon) {
        this.itemName = itemName;
        this.itemCount = itemCount;
        this.itemIcon = itemIcon;
    }

    public String getItemName() {
        return this.itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getItemCount() {
        return this.itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    public int getItemIcon() {
        return this.itemIcon;
    }

    public void setItemIcon(int itemIcon) {
        this.itemIcon = itemIcon;
    }
}
