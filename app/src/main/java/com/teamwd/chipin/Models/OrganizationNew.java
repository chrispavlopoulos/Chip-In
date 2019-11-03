package com.teamwd.chipin.Models;

import android.graphics.drawable.GradientDrawable;

public class OrganizationNew {

    private String ein;
    private String categoryName;
    private String charityName;
    private String mission;
    private String categoryImage ;
    private String causeName ;
    private String state ;
    private int score;

    public OrganizationNew(String ein, String categoryName, String charityName, String mission, String categoryImage, String causeName, String state, int score) {
        this.ein = ein;
        this.categoryName = categoryName;
        this.charityName = charityName;
        this.mission = mission;
        this.categoryImage = categoryImage;
        this.causeName = causeName;
        this.state = state;
        this.score = score;
    }

    public OrganizationNew() {

    }

    public String getEin() {
        return ein;
    }

    public void setEin(String ein) {
        this.ein = ein;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCharityName() {
        return charityName;
    }

    public void setCharityName(String charityName) {
        this.charityName = charityName;
    }

    public String getMission() {
        return mission;
    }

    public void setMission(String mission) {
        this.mission = mission;
    }

    public String getCategoryImage() {
        return categoryImage;
    }

    public void setCategoryImage(String categoryImage) {
        this.categoryImage = categoryImage;
    }

    public String getCauseName() {
        return causeName;
    }

    public void setCauseName(String causeName) {
        this.causeName = causeName;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
