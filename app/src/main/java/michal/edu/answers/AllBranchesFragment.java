package michal.edu.answers;


import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import michal.edu.answers.Adapters.BranchAdapter;
import michal.edu.answers.Listeners.BranchListener;
import michal.edu.answers.Listeners.SectionListener;
import michal.edu.answers.Models.Branch;
import michal.edu.answers.Models.Section;
import michal.edu.answers.Models.Store;


/**
 * A simple {@link Fragment} subclass.
 */
public class AllBranchesFragment extends Fragment {

    private DataSource dataSource = DataSource.getInstance();
    private RecyclerView rvBranches;
    private BranchAdapter adapter;
    private TextView tvBranches, firstLetter;
    private ImageView cardImage;
    private Store thisStore;

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

        setInitialView(v);

//        dataSource.getBranchesFromFirebase(thisStore, new BranchListener() {
//            @Override
//            public void onBranchCallback(ArrayList<Branch> branches) {
//                adapter = new BranchAdapter(thisStore, branches, getActivity());
//                rvBranches.setLayoutManager(new LinearLayoutManager(getContext()));
//                rvBranches.setAdapter(adapter);
//            }
//        });


            //sort branches with ABC
        //if (!thisStore.getBranches().isEmpty()) {
            Collections.sort(thisStore.getBranches(), new Comparator<Branch>() {
                @Override
                public int compare(Branch o1, Branch o2) {
                    return o1.getBranchName().compareTo(o2.getBranchName());
                }
            });
     //   }

            adapter = new BranchAdapter(thisStore, thisStore.getBranches(), getActivity());
            rvBranches.setLayoutManager(new LinearLayoutManager(getContext()));
            rvBranches.setAdapter(adapter);

        return v;
    }

    private void setInitialView(View v){
        rvBranches = v.findViewById(R.id.rvBranches);
        tvBranches = v.findViewById(R.id.tvBranches);
        firstLetter = v.findViewById(R.id.firstLetter);
        cardImage = v.findViewById(R.id.cardImage);

        thisStore = (Store) getArguments().getSerializable("store");

        tvBranches.setText("BRANCHES: " + thisStore.getStoreName());
        dataSource.setStoreLogo(thisStore, cardImage, firstLetter, getContext());

        getActivity().getWindow().setStatusBarColor(Color.parseColor("#ffFEDC32"));
        BottomNavigationView navigation = (BottomNavigationView) getActivity().findViewById(R.id.navigation);
        navigation.setItemIconTintList(ColorStateList.valueOf(Color.parseColor("#ffEA4C5F")));


        View bottomBar = getActivity().getWindow().findViewById(R.id.navigation);
        //bottomBar.clearAnimation();
        bottomBar.animate().translationY(0).setDuration(400);
    }

}
