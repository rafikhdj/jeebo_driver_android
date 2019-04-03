package com.app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

/**
 * This class is used to contain information for login response information.
 */

public class LoginResponse {

    @SerializedName("account_type")
    @Expose
    private String accountType;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("rights")
    @Expose
    private String rights;

    @SerializedName("companyID")
    @Expose
    private String companyID;
    @SerializedName("account_role")
    @Expose
    private String accountRole;

    @SerializedName("allowed_collections")
    @Expose
    private HashMap<String, String> allowed_collections;
    @SerializedName("companyName")
    @Expose
    private String companyName;
    @SerializedName("company_type")
    @Expose
    private String companyType;
    @SerializedName("session_password")
    @Expose
    private String sessionPassword;
    @SerializedName("accountID")
    @Expose
    private String accountID;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @SerializedName("country")
    @Expose
    private String country;



    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRights() {
        return rights;
    }

    public void setRights(String rights) {
        this.rights = rights;
    }


    public String getCompanyID() {
        return companyID;
    }

    public void setCompanyID(String companyID) {
        this.companyID = companyID;
    }

    public String getAccountRole() {
        return accountRole;
    }

    public void setAccountRole(String accountRole) {
        this.accountRole = accountRole;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyType() {
        return companyType;
    }

    public void setCompanyType(String companyType) {
        this.companyType = companyType;
    }

    public String getSessionPassword() {
        return sessionPassword;
    }

    public void setSessionPassword(String sessionPassword) {
        this.sessionPassword = sessionPassword;
    }

    public String getAccountID() {
        return accountID;
    }

    public void setAccountID(String accountID) {
        this.accountID = accountID;
    }

    public HashMap<String, String> getAllowed_collections() {
        return allowed_collections;
    }

    public void setAllowed_collections(HashMap<String, String> allowed_collections) {
        this.allowed_collections = allowed_collections;
    }
}
