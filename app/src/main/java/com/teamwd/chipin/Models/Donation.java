package com.teamwd.chipin.Models;

public class Donation {

    private String charityName;
    private double amount;
    private String donationTitle;
    private String userComment;
    private long timeInMillis;

    public Donation(String charityName, double amount, String donationTitle, String userComment, long timeInMillis) {
        this.charityName = charityName;
        this.amount = amount;
        this.donationTitle = donationTitle;
        this.userComment = userComment;
        this.timeInMillis = timeInMillis;
    }

    public String getCharityName() {
        return charityName;
    }

    public void setCharityName(String charityName) {
        this.charityName = charityName;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public long getTimeInMillis() {
        return timeInMillis;
    }

    public void setTimeInMillis(long timeInMillis) {
        this.timeInMillis = timeInMillis;
    }

    public String getDonationTitle() {
        return donationTitle;
    }

    public void setDonationTitle(String donationTitle) {
        this.donationTitle = donationTitle;
    }

    public String getUserComment() {
        return userComment;
    }

    public void setUserComment(String userComment) {
        this.userComment = userComment;
    }
}
