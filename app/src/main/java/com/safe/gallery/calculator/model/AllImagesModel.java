package com.safe.gallery.calculator.model;

import android.os.Parcel;
import android.os.Parcelable;

public class AllImagesModel implements Parcelable {
    public static final Creator<AllImagesModel> CREATOR = new C06171();
    private int imageId;
    private long imageLastModified;
    private String imagePath;
    private boolean isEditable = false;
    private boolean isSelected = false;
    private String newPath;

    public AllImagesModel() {

    }

    static class C06171 implements Creator<AllImagesModel> {
        C06171() {
        }

        public AllImagesModel createFromParcel(Parcel source) {
            return new AllImagesModel(source);
        }

        public AllImagesModel[] newArray(int size) {
            return new AllImagesModel[size];
        }
    }

    public boolean isEditable() {
        return this.isEditable;
    }

    public void setEditable(boolean editable) {
        this.isEditable = editable;
    }

    public AllImagesModel(String imagePath, long imageLastModified) {
        this.imagePath = imagePath;
        this.imageLastModified = imageLastModified;
    }

    public AllImagesModel(String imagePath, String newPath) {
        this.imagePath = imagePath;
        this.newPath = newPath;
    }

    public AllImagesModel(int imageId, String imagePath, String newPath) {
        this.imageId = imageId;
        this.imagePath = imagePath;
        this.newPath = newPath;
    }

    public int getImageId() {
        return this.imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getNewPath() {
        return this.newPath;
    }

    public void setNewPath(String newPath) {
        this.newPath = newPath;
    }

    public static Creator<AllImagesModel> getCREATOR() {
        return CREATOR;
    }

    public long getImageLastModified() {
        return this.imageLastModified;
    }

    public void setImageLastModified(long imageLastModified) {
        this.imageLastModified = imageLastModified;
    }

    public String getImagePath() {
        return this.imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public boolean isSelected() {
        return this.isSelected;
    }

    public void setSelected(boolean selected) {
        this.isSelected = selected;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        byte b;
        byte b2 = (byte) 1;
        dest.writeInt(this.imageId);
        dest.writeString(this.imagePath);
        dest.writeString(this.newPath);
        if (this.isSelected) {
            b = (byte) 1;
        } else {
            b = (byte) 0;
        }
        dest.writeByte(b);
        if (!this.isEditable) {
            b2 = (byte) 0;
        }
        dest.writeByte(b2);
        dest.writeLong(this.imageLastModified);
    }

    protected AllImagesModel(Parcel in) {
        boolean z;
        boolean z2 = true;
        this.imageId = in.readInt();
        this.imagePath = in.readString();
        this.newPath = in.readString();
        if (in.readByte() != (byte) 0) {
            z = true;
        } else {
            z = false;
        }
        this.isSelected = z;
        if (in.readByte() == (byte) 0) {
            z2 = false;
        }
        this.isEditable = z2;
        this.imageLastModified = in.readLong();
    }
}
