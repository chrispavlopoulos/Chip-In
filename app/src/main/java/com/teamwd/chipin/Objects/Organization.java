package com.teamwd.chipin.Objects;

import io.realm.RealmObject;

public class Organization extends RealmObject {

    private String ein;
    private int orgId;
    private String charityName;
    private String tagLine;
    private String websiteUrl;
    private String charityNavigatorUrl;
    private String mission;
    private String phoneNumber;
    private String generalEmail;
    private OrganizationCategory category;
    private OrganizationCause cause;
    private IrsClassification irsClassification;
    private OrganizationAddress mailingAddress;
    private OrganizationAddress donationAddress;

    public Organization() { }

    public String getEin() {
        return ein;
    }

    public void setEin(String ein) {
        this.ein = ein;
    }

    public int getOrgId() {
        return orgId;
    }

    public void setOrgId(int orgId) {
        this.orgId = orgId;
    }

    public String getCharityName() {
        return charityName;
    }

    public void setCharityName(String charityName) {
        this.charityName = charityName;
    }

    public String getTagLine() {
        return tagLine;
    }

    public void setTagLine(String tagLine) {
        this.tagLine = tagLine;
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    public String getCharityNavigatorUrl() {
        return charityNavigatorUrl;
    }

    public void setCharityNavigatorUrl(String charityNavigatorUrl) {
        this.charityNavigatorUrl = charityNavigatorUrl;
    }

    public String getMission() {
        return mission;
    }

    public void setMission(String mission) {
        this.mission = mission;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getGeneralEmail() {
        return generalEmail;
    }

    public void setGeneralEmail(String generalEmail) {
        this.generalEmail = generalEmail;
    }

    public OrganizationCategory getCategory() {
        return category;
    }

    public void setCategory(OrganizationCategory category) {
        this.category = category;
    }

    public OrganizationCause getCause() {
        return cause;
    }

    public void setCause(OrganizationCause cause) {
        this.cause = cause;
    }

    public IrsClassification getIrsClassification() {
        return irsClassification;
    }

    public void setIrsClassification(IrsClassification irsClassification) {
        this.irsClassification = irsClassification;
    }

    public OrganizationAddress getMailingAddress() {
        return mailingAddress;
    }

    public void setMailingAddress(OrganizationAddress mailingAddress) {
        this.mailingAddress = mailingAddress;
    }

    public OrganizationAddress getDonationAddress() {
        return donationAddress;
    }

    public void setDonationAddress(OrganizationAddress donationAddress) {
        this.donationAddress = donationAddress;
    }
}
