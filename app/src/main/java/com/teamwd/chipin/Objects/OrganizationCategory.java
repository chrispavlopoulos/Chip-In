package com.teamwd.chipin.Objects;

import io.realm.RealmObject;

public class OrganizationCategory extends RealmObject {

    private int categoryId;
    private String categoryName;
    private String image;

    public OrganizationCategory() {

    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
