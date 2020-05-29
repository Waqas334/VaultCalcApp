package com.androidbull.calculator.photo.vault.model;

import android.os.Parcel;
import android.os.Parcelable;

public class AllVideosModel implements Parcelable {
    public static final Creator<AllVideosModel> CREATOR = new C06181();
    private boolean isEditable = false;
    private boolean isSelected = false;
    private long lastModified;
    private String newPath;
    private String oldPath;
    private int videoId;

    public AllVideosModel() {

    }

    static class C06181 implements Creator<AllVideosModel> {
        C06181() {
        }

        public AllVideosModel createFromParcel(Parcel source) {
            return new AllVideosModel(source);
        }

        public AllVideosModel[] newArray(int size) {
            return new AllVideosModel[size];
        }
    }

    public AllVideosModel(String imagePath, long imageLastModified) {
        this.oldPath = imagePath;
        this.lastModified = imageLastModified;
    }

    public AllVideosModel(String imagePath, String newPath) {
        this.oldPath = imagePath;
        this.newPath = newPath;
    }

    public AllVideosModel(int imageId, String imagePath, String newPath) {
        this.videoId = imageId;
        this.oldPath = imagePath;
        this.newPath = newPath;
    }

    public boolean isEditable() {
        return this.isEditable;
    }

    public void setEditable(boolean editable) {
        this.isEditable = editable;
    }

    public int getVideoId() {
        return this.videoId;
    }

    public void setVideoId(int videoId) {
        this.videoId = videoId;
    }

    public String getNewPath() {
        return this.newPath;
    }

    public void setNewPath(String newPath) {
        this.newPath = newPath;
    }

    public static Creator<AllVideosModel> getCREATOR() {
        return CREATOR;
    }

    public long getLastModified() {
        return this.lastModified;
    }

    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }

    public String getOldPath() {
        return this.oldPath;
    }

    public void setOldPath(String oldPath) {
        this.oldPath = oldPath;
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
        dest.writeString(this.oldPath);
        dest.writeString(this.newPath);
        dest.writeByte(this.isSelected ? (byte) 1 : (byte) 0);
        dest.writeLong(this.lastModified);
    }

    protected AllVideosModel(Parcel in) {
        boolean z = false;
        this.oldPath = in.readString();
        this.newPath = in.readString();
        if (in.readByte() != (byte) 0) {
            z = true;
        }
        this.isSelected = z;
        this.lastModified = in.readLong();
    }
}
