package com.teamwd.chipin.Objects;

import io.realm.RealmObject;

<<<<<<< HEAD
public class IrsClassification extends RealmObject {

    private String subSection;
    private String foundationStatus;
    private String deductibility;
    private String deductibilityCode;
    private int assetAmount;
    private int incomeAmount;
    private String filingRequirement;
    private String classification;

    public String getDeductibilityCode() {
        return deductibilityCode;
    }

    public void setDeductibilityCode(String deductibilityCode) {
        this.deductibilityCode = deductibilityCode;
    }

    public String getFilingRequirement() {
        return filingRequirement;
    }

    public void setFilingRequirement(String filingRequirement) {
        this.filingRequirement = filingRequirement;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public int getIncomeAmount() {
        return incomeAmount;
    }

    public void setIncomeAmount(int incomeAmount) {
        this.incomeAmount = incomeAmount;
    }

    public int getAssetAmount() {
        return assetAmount;
    }

    public void setAssetAmount(int assetAmount) {
        this.assetAmount = assetAmount;
    }

    public IrsClassification() {

    }

    public String getSubSection() {
        return subSection;
    }

    public void setSubSection(String subSection) {
        this.subSection = subSection;
    }

    public String getFoundationStatus() {
        return foundationStatus;
    }

    public void setFoundationStatus(String foundationStatus) {
        this.foundationStatus = foundationStatus;
    }

    public String getDeductibility() {
        return deductibility;
    }

    public void setDeductibility(String deductability) {
        this.deductibility = deductability;
    }
=======
class IrsClassification extends RealmObject {

    private String subSection;
    private String foundationStatus;
    private String deductability;
    private String deductabilityDetail;
    private String nteeCode;
    private String nteeClassification;
    private String nteeLetter;
    private String nteeType;
    private String nteeSuffix;
>>>>>>> ed679443146b1c1d2f376fafca7c96b699ebf397
}
