package michal.edu.answers.Branch;


import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import michal.edu.answers.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class AllBranchesFragment extends Fragment {

    private RecyclerView rvBranches;
    private BranchAdapter adapter;

    public static AllBranchesFragment newInstance(String ownerID) {

        Bundle args = new Bundle();
        args.putSerializable("ownerId", ownerID);
        AllBranchesFragment fragment = new AllBranchesFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_all_branches, container, false);

        String thisOwnerId = (String) getArguments().getSerializable("ownerId");

        rvBranches = v.findViewById(R.id.rvBranches);
        getBranchesFromFirebase(thisOwnerId, new BranchListener() {
            @Override
            public void onBranchCallback(ArrayList<Branch> branches) {
                adapter = new BranchAdapter(branches, getActivity());
                rvBranches.setLayoutManager(new LinearLayoutManager(getContext()));
                rvBranches.setAdapter(adapter);
                System.out.println(branches);
            }
        });


        getActivity().getWindow().setStatusBarColor(Color.parseColor("#ffFEDC32"));
        BottomNavigationView navigation = (BottomNavigationView) getActivity().findViewById(R.id.navigation);
        navigation.setItemIconTintList(ColorStateList.valueOf(Color.parseColor("#ffEA4C5F")));

        return v;
    }


    public ArrayList<Branch> getBranchesFromFirebase(String ownerId, final BranchListener callback){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Branches").child(ownerId);
        final ArrayList<Branch> mBranches = new ArrayList<>();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Branch value = snapshot.getValue(Branch.class);
                    mBranches.add(value);
                }

                if (mBranches.isEmpty()){
                    System.out.println("no branches");
                }else {
                    callback.onBranchCallback(mBranches);
                    //System.out.println(mBranches);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return mBranches;
    }
}
