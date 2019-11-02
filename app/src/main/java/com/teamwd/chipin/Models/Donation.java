package com.teamwd.chipin.Models;

public class Donation {

    private String charityName;
    private double amount;
    private long timeInMillis;
    private String EIN;

    public Donation(String charityName, double amount, long timeInMillis, String EIN) {
        this.charityName = charityName;
        this.amount = amount;
        this.timeInMillis = timeInMillis;
        this.EIN = EIN;
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

    public String getEIN() {
        return EIN;
    }

    public void setEIN(String EIN) {
        this.EIN = EIN;
    }
}
