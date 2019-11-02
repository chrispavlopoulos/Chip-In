package com.teamwd.chipin.Models;

public class Post {

    public String postText;
    public long timeInMillis;

    public Post(String postText, long timeInMillis) {
        this.postText = postText;
        this.timeInMillis = timeInMillis;
    }

    public String getPostText() {
        return postText;
    }

    public void setPostText(String postText) {
        this.postText = postText;
    }

    public long getTimeInMillis() {
        return timeInMillis;
    }

    public void setTimeInMillis(long timeInMillis) {
        this.timeInMillis = timeInMillis;
    }
}
