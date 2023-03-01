package com.example.athenaclient.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.athenaclient.BranchInterface;
import com.example.athenaclient.R;
import com.example.athenaclient.model.Branch;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;


public class BranchAdapter extends RecyclerView.Adapter<BranchAdapter.MyViewHolder> {

    private final BranchInterface recyclerViewInterface;

    Context context;
    ArrayList<Branch> branchArrayList;

    public BranchAdapter(Context context, ArrayList<Branch> branchArrayList, BranchInterface recyclerViewInterface) {
        this.context = context;
        this.branchArrayList = branchArrayList;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    public void updateBranches(ArrayList<Branch> branches){
        this.branchArrayList = branches;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BranchAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.branch_item, parent, false);
        return new MyViewHolder(v, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull BranchAdapter.MyViewHolder holder, int position) {
        Branch branch = branchArrayList.get(position);

        holder.branchName.setText(branch.getBranchName());
        holder.branchHours.setText(branch.getBranchHours());
        holder.branchLocation.setText(branch.getBranchLocation());

        Glide.with(context)
                .load(branch.getBranchPhoto())
                .into(holder.branchPhoto);
    }

    @Override
    public int getItemCount() {
        return branchArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        RoundedImageView branchPhoto;
        TextView branchHours, branchLocation, branchName;

        public MyViewHolder(@NonNull View itemView, BranchInterface recyclerViewInterface) {
            super(itemView);
            branchPhoto = itemView.findViewById(R.id.branchPhoto);
            branchHours = itemView.findViewById(R.id.branchHours);
            branchLocation = itemView.findViewById(R.id.branchLocation);
            branchName = itemView.findViewById(R.id.branchName);

            itemView.setOnClickListener(view -> {
                if (recyclerViewInterface != null) {
                    int pos = getAdapterPosition();

                    if (pos != RecyclerView.NO_POSITION) {
                        recyclerViewInterface.onItemClick(pos);
                    }

                }
            });
        }
    }
}
