package com.example.athenaclient.model;

import java.util.ArrayList;
import java.util.HashMap;

public class Booking {
    String branch, client, date, status, time;
    int total;
    ArrayList<HashMap<String, Object>> services;

    public Booking(String branch, String client, String date, String status, String time, int total, ArrayList<HashMap<String, Object>> services) {
        this.branch = branch;
        this.client = client;
        this.date = date;
        this.status = status;
        this.time = time;
        this.total = total;
        this.services = services;
    }

    public Booking() {
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public ArrayList<HashMap<String, Object>> getServices() {
        return services;
    }

    public void setServices(ArrayList<HashMap<String, Object>> services) {
        this.services = services;
    }
}
