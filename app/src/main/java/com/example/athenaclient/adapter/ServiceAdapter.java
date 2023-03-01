package com.example.athenaclient.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.athenaclient.BranchInterface;
import com.example.athenaclient.R;
import com.example.athenaclient.ServiceInterface;
import com.example.athenaclient.model.Service;


import java.util.ArrayList;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.MyViewHolder>{

    private final ServiceInterface recyclerViewInterface;

    Context context;
    ArrayList<Service> serviceArrayList;

    public ServiceAdapter(Context context, ArrayList<Service> serviceArrayList, ServiceInterface recyclerViewInterface) {
        this.context = context;
        this.serviceArrayList = serviceArrayList;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    public void updateServices(ArrayList<Service> services){
        this.serviceArrayList = services;
        notifyDataSetChanged();
    }



    @NonNull
    @Override
    public ServiceAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.service_item, parent, false);
            return new ServiceAdapter.MyViewHolder(v, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceAdapter.MyViewHolder holder, int position) {
        Service service = serviceArrayList.get(position);
        holder.serviceName.setText(service.getServiceName());
        holder.serviceCost.setText(String.valueOf(service.getServiceCost()));
        if (service.isChecked()){
            holder.llTickImage.setVisibility(View.VISIBLE);
        }else {
            holder.llTickImage.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(v -> {
            service.setChecked(!service.isChecked());
            if (service.isChecked()){
                holder.llTickImage.setVisibility(View.VISIBLE);
            }else {
                holder.llTickImage.setVisibility(View.GONE);
            }
            if (recyclerViewInterface!=null){
                recyclerViewInterface.onItemClick(service);
            }
        });

       /* holder.itemView.setOnClickListener(view -> {
            if (recyclerViewInterface != null) {
                int pos = getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    recyclerViewInterface.onItemClick(pos);
                }

            }
        });*/
    }

    @Override
    public int getItemCount() {
        return serviceArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView serviceName, serviceCost;
        LinearLayout llTickImage;

        public MyViewHolder(@NonNull View itemView, ServiceInterface recyclerViewInterface) {
            super(itemView);
            serviceName = itemView.findViewById(R.id.txtServiceName);
            serviceCost = itemView.findViewById(R.id.txtServiceCost);
            llTickImage=  itemView.findViewById(R.id.llTickImage);
        }
    }
}
