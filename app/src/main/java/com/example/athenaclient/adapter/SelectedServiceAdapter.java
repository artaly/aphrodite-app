package com.example.athenaclient.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.athenaclient.R;
import com.example.athenaclient.ServiceInterface;
import com.example.athenaclient.model.Service;

import java.util.ArrayList;

public class SelectedServiceAdapter extends RecyclerView.Adapter<SelectedServiceAdapter.MyViewHolder>{

    private final ServiceInterface recyclerViewInterface;

    Context context;
    ArrayList<Service> serviceArrayList;

    public SelectedServiceAdapter(Context context, ArrayList<Service> serviceArrayList, ServiceInterface recyclerViewInterface) {
        this.context = context;
        this.serviceArrayList = serviceArrayList;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public SelectedServiceAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.selected_service_item, parent, false);
            return new SelectedServiceAdapter.MyViewHolder(v, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull SelectedServiceAdapter.MyViewHolder holder, int position) {
        Service service = serviceArrayList.get(position);
        holder.serviceName.setText(service.getServiceName());
        String cost=String.valueOf(service.getServiceCost());
        holder.serviceCost.setText("₱ "+cost);
        holder.itemView.setOnClickListener(v -> {

        });

    }

    @Override
    public int getItemCount() {
        return serviceArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView serviceName, serviceCost;

        public MyViewHolder(@NonNull View itemView, ServiceInterface recyclerViewInterface) {
            super(itemView);
            serviceName = itemView.findViewById(R.id.txtServiceName);
            serviceCost = itemView.findViewById(R.id.txtServiceCost);
        }
    }
}
