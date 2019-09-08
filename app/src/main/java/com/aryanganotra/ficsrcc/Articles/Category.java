package com.aryanganotra.ficsrcc.Articles;

import android.os.Parcel;
import android.os.Parcelable;

public class Category implements Parcelable {
    public String name;
    public int id;
    public int count;

    protected Category(Parcel in) {
        name = in.readString();
        id = in.readInt();
        count = in.readInt();
    }

    public static final Creator<Category> CREATOR = new Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel in) {
            return new Category(in);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Category(String name, int id, int count){
        this.name=name;
        this.id=id;
        this.count=count;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(id);
        dest.writeInt(count);
    }
}
