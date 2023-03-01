package com.example.athenaclient.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Service implements Parcelable {
    private Boolean isChedked = false;
    String serviceName, keywords;
    int serviceCost;

    public Service() {

    }
    int searchMatches;

    public void inrSearchMatch(){
        searchMatches++;
    }

    public void inrSearchMatch(int inr){
        searchMatches += inr;
    }

    public int compareTo(Service b)
    {
        return(b.searchMatches - searchMatches);
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



    public boolean isChecked() {
        return isChedked;
    }


    public void setChecked(boolean checked) {
        isChedked = checked;
    }

    public Service(String serviceName, int serviceCost, String keywords) {
        this.serviceName = serviceName;
        this.serviceCost = serviceCost;
        this.keywords= keywords;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public int getServiceCost() {
        return serviceCost;
    }

    public void setServiceCost(int serviceCost) {
        this.serviceCost = serviceCost;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.isChedked);
        dest.writeString(this.serviceName);
        dest.writeInt(this.serviceCost);
    }

    public void readFromParcel(Parcel source) {
        this.isChedked = (Boolean) source.readValue(Boolean.class.getClassLoader());
        this.serviceName = source.readString();
        this.serviceCost = source.readInt();
    }

    protected Service(Parcel in) {
        this.isChedked = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.serviceName = in.readString();
        this.serviceCost = in.readInt();
    }

    public static final Parcelable.Creator<Service> CREATOR = new Parcelable.Creator<Service>() {
        @Override
        public Service createFromParcel(Parcel source) {
            return new Service(source);
        }

        @Override
        public Service[] newArray(int size) {
            return new Service[size];
        }
    };
}
