package com.teamwd.chipin.Objects;

import io.realm.RealmObject;

public class OrganizationCause extends RealmObject {

    private int causeId;
    private String causeName;
    private String image;

    public OrganizationCause() {

    }

    public int getCauseId() {
        return causeId;
    }

    public void setCauseId(int causeId) {
        this.causeId = causeId;
    }

    public String getCauseName() {
        return causeName;
    }

    public void setCauseName(String causeName) {
        this.causeName = causeName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
