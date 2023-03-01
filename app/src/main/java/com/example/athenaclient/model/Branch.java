package com.example.athenaclient.model;

public class Branch {
    String branchHours, branchLocation, branchName, branchPhoto, keywords;
    int searchMatches;

    public void inrSearchMatch(){
        searchMatches++;
    }

    public void inrSearchMatch(int inr){
        searchMatches += inr;
    }

    public int compareTo(Branch b)
    {
        return(b.searchMatches - searchMatches);
    }

    public Branch(String branchHours, String branchLocation, String branchName, String branchPhoto) {
        this.branchHours = branchHours;
        this.branchLocation = branchLocation;
        this.branchName = branchName;
        this.branchPhoto = branchPhoto;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public int getSearchMatches() {
        return searchMatches;
    }

    public void setSearchMatches(int searchMatches) {
        this.searchMatches = searchMatches;
    }

    public Branch(String branchHours, String branchLocation, String branchName, String branchPhoto, String keywords) {
        this.branchHours = branchHours;
        this.branchLocation = branchLocation;
        this.branchName = branchName;
        this.branchPhoto = branchPhoto;
        this.keywords = keywords;
    }


    public Branch() {

    }

    public String getBranchHours() {
        return branchHours;
    }

    public void setBranchHours(String branchHours) {
        this.branchHours = branchHours;
    }

    public String getBranchLocation() {
        return branchLocation;
    }

    public void setBranchLocation(String branchLocation) {
        this.branchLocation = branchLocation;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getBranchPhoto() {
        return branchPhoto;
    }

    public void setBranchPhoto(String branchPhoto) {
        this.branchPhoto = branchPhoto;
    }
}
