package michal.edu.answers.Branch;


import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import michal.edu.answers.DataSource;
import michal.edu.answers.Models.Branch;
import michal.edu.answers.Models.Section;
import michal.edu.answers.MyImageStorage;
import michal.edu.answers.R;
import michal.edu.answers.Models.Store;
import michal.edu.answers.SectionListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class AllBranchesFragment extends Fragment {

    private RecyclerView rvBranches;
    private BranchAdapter adapter;
    private TextView tvBranches, firstLetter;
    private ImageView cardImage;
    private DataSource dataSource = DataSource.getInstance();

    public static AllBranchesFragment newInstance(Store store) {

        Bundle args = new Bundle();
        args.putSerializable("store", store);
        AllBranchesFragment fragment = new AllBranchesFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_all_branches, container, false);

        rvBranches = v.findViewById(R.id.rvBranches);
        tvBranches = v.findViewById(R.id.tvBranches);
        firstLetter = v.findViewById(R.id.firstLetter);
        cardImage = v.findViewById(R.id.cardImage);

        final Store thisStore = (Store) getArguments().getSerializable("store");

        tvBranches.setText("BRANCHES: " + thisStore.getStoreName());
        dataSource.setStoreLogo(thisStore, cardImage, firstLetter, getContext());

        dataSource.getBranchesFromFirebase(thisStore, new BranchListener() {
            @Override
            public void onBranchCallback(ArrayList<Branch> branches) {
                adapter = new BranchAdapter(thisStore, branches, getActivity());
                rvBranches.setLayoutManager(new LinearLayoutManager(getContext()));
                rvBranches.setAdapter(adapter);
            }
        });

        getActivity().getWindow().setStatusBarColor(Color.parseColor("#ffFEDC32"));
        BottomNavigationView navigation = (BottomNavigationView) getActivity().findViewById(R.id.navigation);
        navigation.setItemIconTintList(ColorStateList.valueOf(Color.parseColor("#ffEA4C5F")));

        return v;
    }

}
