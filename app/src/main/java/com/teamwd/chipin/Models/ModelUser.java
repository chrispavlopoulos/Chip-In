package com.teamwd.chipin.Models;

import java.util.ArrayList;

public class ModelUser {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private ArrayList<Donation> donationList;
    private ArrayList<Post> postList;
    private long score;
    private ArrayList<String> badges;
    private boolean isCompany;


    public ModelUser(String firstName, String lastName, String email, String password, boolean isCompany) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.isCompany = isCompany;
        donationList = new ArrayList<>();
        postList = new ArrayList<>();
        badges = new ArrayList<>();
    }

    public boolean isCompany() {
        return isCompany;
    }

    public void setCompany(boolean company) {
        isCompany = company;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ArrayList<Donation> getDonationList() {
        return donationList;
    }

    public void addDonation(Donation donation){
        donationList.add(donation);
    }

    public void removeDonation(Donation donation){
        donationList.remove(donation);
    }

    public void setDonationList(ArrayList<Donation> donation) {
        this.donationList = donation;
    }

    public ArrayList<Post> getPostList() {
        return postList;
    }

    public void setPostList(ArrayList<Post> postList) {
        this.postList = postList;
    }

    public void addPost(Post post){
        postList.add(post);
    }

    public void removePost(Post post){
        postList.remove(post);
    }

    public long getScore() {
        return score;
    }

    public void setScore(long score) {
        this.score = score;
    }

    public ArrayList<String> getBadges() {
        return badges;
    }

    public void setBadges(ArrayList<String> badges) {
        this.badges = badges;
    }

    public void addBadge(String badge){
        badges.add(badge);
    }

    public void removeBadge(String badge){
        badges.remove(badge);
    }


}
