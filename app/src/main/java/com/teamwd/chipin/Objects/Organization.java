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
    private AdvisoryCollection activeAdvisories;
    private AdvisoryCollection removedAdvisories;
    private Representative currentBoardChair;
    private Representative currentCeo;
    private RatingLink currentRating;
    private RatingCollection ratingHistory;

    public Organization() {

    }
}
