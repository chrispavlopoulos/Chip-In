package com.teamwd.chipin.Models;

import java.io.Serializable;

public class Event implements Serializable {

    private String companyName;
    private String evenTitle;
    private String eventDetails;
    private long amountContributed;
    private float percentToMatch;
    private long startTime;
    private long endTime;
    private String goal;

    public Event(String companyName, String evenTitle, String eventDetails, long amountContributed, float percentToMatch, long startTime, long endTime, String goal) {
        this.companyName = companyName;
        this.evenTitle = evenTitle;
        this.eventDetails = eventDetails;
        this.amountContributed = amountContributed;
        this.percentToMatch = percentToMatch;
        this.startTime = startTime;
        this.endTime = endTime;
        this.goal = goal;
    }

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getEvenTitle() {
        return evenTitle;
    }

    public void setEvenTitle(String evenTitle) {
        this.evenTitle = evenTitle;
    }

    public String getEventDetails() {
        return eventDetails;
    }

    public void setEventDetails(String eventDetails) {
        this.eventDetails = eventDetails;
    }

    public long getAmountContributed() {
        return amountContributed;
    }

    public void setAmountContributed(long amountContributed) {
        this.amountContributed = amountContributed;
    }

    public float getPercentToMatch() {
        return percentToMatch;
    }

    public void setPercentToMatch(float percentToMatch) {
        this.percentToMatch = percentToMatch;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }
}
