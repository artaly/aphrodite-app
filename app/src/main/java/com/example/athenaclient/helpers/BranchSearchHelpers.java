package com.example.athenaclient.helpers;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.example.athenaclient.adapter.BranchAdapter;
import com.example.athenaclient.model.Branch;

import java.util.ArrayList;
import java.util.Collections;

public class BranchSearchHelpers {

    private static ArrayList<Branch> filterBranches(ArrayList<Branch> branches, String keyword){
        ArrayList<Branch> filteredBranches = new ArrayList<>();
        keyword = keyword.toLowerCase().trim();
        for (Branch branch: branches){
            branch.setSearchMatches(0);
            if ((branch.getBranchName() != null &&  branch.getBranchName().toLowerCase().contains(keyword))
            || (branch.getBranchLocation() != null && branch.getBranchLocation().toLowerCase().contains(keyword))
            || (branch.getKeywords() != null && branch.getKeywords().toLowerCase().contains(keyword))
            || (branch.getBranchHours() != null && branch.getBranchHours().toLowerCase().contains(keyword))
            || (branch.getBranchPhoto() != null && branch.getBranchPhoto().toLowerCase().contains(keyword))){
                filteredBranches.add(branch);
                if (branch.getBranchName() != null &&
                        branch.getBranchName().toLowerCase().contains(keyword))
                    branch.inrSearchMatch(10);
                if(branch.getBranchLocation() != null &&
                        branch.getBranchLocation().toLowerCase().contains(keyword))
                    branch.inrSearchMatch(7);
                if(branch.getKeywords() != null &&
                        branch.getKeywords().toLowerCase().contains(keyword))
                    branch.inrSearchMatch(5);
                if(branch.getBranchHours() != null &&
                        branch.getBranchHours().toLowerCase().contains(keyword))
                    branch.inrSearchMatch(1);
            }
        }
        Collections.sort(filteredBranches, Branch::compareTo);
        return filteredBranches;
    }

    public static void implementBranchSearch(EditText searchEt, BranchAdapter adapter, ArrayList<Branch> branches){
        searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = String.valueOf(s);
                if (text.isEmpty())
                    resetSearch(adapter, branches);
                else
                    search(adapter, branches, text);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private static void search(BranchAdapter adapter, ArrayList<Branch> branches, String keyword) {
        ArrayList<Branch> filteredBranches = filterBranches(branches, keyword);
        adapter.updateBranches(filteredBranches);
    }

    private static void resetSearch(BranchAdapter adapter, ArrayList<Branch> branches) {
        adapter.updateBranches(branches);
    }
}
