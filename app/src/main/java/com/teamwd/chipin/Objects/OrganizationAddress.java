package com.teamwd.chipin.Objects;

import io.realm.RealmObject;

public class OrganizationAddress extends RealmObject {

    private String streetAddress1;
    private String streetAddress2;
    private String city;
    private String stateOrProvince;
    private String postalCode;
    private String country;

    public  OrganizationAddress() {

    }

    public String getStreetAddress1() {
        return streetAddress1;
    }

    public void setStreetAddress1(String streetAddress1) {
        this.streetAddress1 = streetAddress1;
    }

    public String getStreetAddress2() {
        return streetAddress2;
    }

    public void setStreetAddress2(String streetAddress2) {
        this.streetAddress2 = streetAddress2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStateOrProvince() {
        return stateOrProvince;
    }

    public void setStateOrProvince(String stateOrProvince) {
        this.stateOrProvince = stateOrProvince;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
