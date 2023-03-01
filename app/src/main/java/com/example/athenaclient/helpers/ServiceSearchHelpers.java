package com.example.athenaclient.helpers;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.example.athenaclient.adapter.ServiceAdapter;
import com.example.athenaclient.model.Service;

import java.util.ArrayList;
import java.util.Collections;

public class ServiceSearchHelpers {
    private static ArrayList<Service> filterservicees(ArrayList<Service> services, String keyword){
        ArrayList<Service> filteredservicees = new ArrayList<>();
        keyword = keyword.toLowerCase().trim();
        for (Service service: services){
            service.setSearchMatches(0);
            if ((service.getServiceName() != null &&  service.getServiceName().toLowerCase().contains(keyword))){
                filteredservicees.add(service);
                if (service.getServiceName() != null &&
                        service.getServiceName().toLowerCase().contains(keyword))
                    service.inrSearchMatch(10);
            }
        }
        Collections.sort(filteredservicees, Service::compareTo);
        return filteredservicees;
    }

    public static void implementserviceSearch(EditText searchEt, ServiceAdapter adapter, ArrayList<Service> services){
        searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = String.valueOf(s);
                if (text.isEmpty())
                    resetSearch(adapter, services);
                else
                    search(adapter, services, text);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private static void search(ServiceAdapter adapter, ArrayList<Service> services, String keyword) {
        ArrayList<Service> filteredservices = filterservicees(services, keyword);
        adapter.updateServices(filteredservices);
    }

    private static void resetSearch(ServiceAdapter adapter, ArrayList<Service> services) {
        adapter.updateServices(services);
    }
}
