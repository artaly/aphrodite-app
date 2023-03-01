package com.example.athenaclient.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.athenaclient.R;
import com.example.athenaclient.model.Service;
import com.google.common.collect.ArrayTable;

import java.util.ArrayList;

public class MultiAdapter extends RecyclerView.Adapter<MultiAdapter.MultiViewHolder> {

    private Context context;
    private ArrayList<Service> services;

    public MultiAdapter(Context context,  ArrayList<Service> services) {
        this.context = context;
        this.services = services;
    }

    public void setServices(ArrayList<Service> services) {
        this.services = new ArrayList<>();
        this.services =  services;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MultiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.service_item, parent, false);
        return new MultiViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MultiViewHolder holder, int position) {
        holder.bind(services.get(position));
    }

    @Override
    public int getItemCount() {
        return services.size();
    }


    class MultiViewHolder extends RecyclerView.ViewHolder {


        TextView serviceName, serviceCost;
        ImageView selectedCheck;

        public ArrayList<Service> getAll() {
            return services;
        }



        public ArrayList<Service> getSelected() {
            ArrayList<Service> selected = new ArrayList<>();
            for (int i=0; i<services.size(); i++) {
                if (services.get(i).isChecked()) {
                    selected.add(services.get(i));
                }
            }
            return services;
        }



        public MultiViewHolder(@NonNull View itemView) {
            super(itemView);

            serviceName = itemView.findViewById(R.id.txtServiceName);
            serviceCost = itemView.findViewById(R.id.txtServiceCost);
            selectedCheck = itemView.findViewById(R.id.checkedServiceImg);
        }

        void bind(final Service service) {
            selectedCheck.setVisibility(service.isChecked() ? View.VISIBLE : View.GONE);
            serviceName.setText(service.getServiceName());

            itemView.setOnClickListener(view -> {
                service.setChecked(!service.isChecked());
                selectedCheck.setVisibility(service.isChecked() ? View.VISIBLE : View.GONE);

            });

        }
    }
}
