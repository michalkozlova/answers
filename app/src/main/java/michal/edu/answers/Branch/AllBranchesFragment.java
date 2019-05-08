package michal.edu.answers.Branch;


import android.content.res.ColorStateList;
import android.graphics.Color;
import android.media.Image;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import michal.edu.answers.DataSource;
import michal.edu.answers.MyImageStorage;
import michal.edu.answers.R;
import michal.edu.answers.Stores.Store;


/**
 * A simple {@link Fragment} subclass.
 */
public class AllBranchesFragment extends Fragment {

    private RecyclerView rvBranches;
    private BranchAdapter adapter;
    private TextView tvBranches, firstLetter;
    private ImageView cardImage;

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

        final Store thisStore = (Store) getArguments().getSerializable("store");

        rvBranches = v.findViewById(R.id.rvBranches);
        tvBranches = v.findViewById(R.id.tvBranches);
        firstLetter = v.findViewById(R.id.firstLetter);
        cardImage = v.findViewById(R.id.cardImage);

        new DataSource().getBranchesFromFirebase(thisStore.getOwnerId(), new BranchListener() {
            @Override
            public void onBranchCallback(ArrayList<Branch> branches) {
                adapter = new BranchAdapter(branches, getActivity());
                rvBranches.setLayoutManager(new LinearLayoutManager(getContext()));
                rvBranches.setAdapter(adapter);
            }
        });

        tvBranches.setText("BRANCHES: " + thisStore.getStoreNameEng());
        StorageReference storageRef = MyImageStorage.getInstance();

        storageRef.child(thisStore.getStoreNameEng()+".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(getContext()).load(uri).into(cardImage);
                firstLetter.setText("");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                firstLetter.setText(Character.toString(thisStore.getStoreNameEng().charAt(0)));
            }
        });

        getActivity().getWindow().setStatusBarColor(Color.parseColor("#ffFEDC32"));
        BottomNavigationView navigation = (BottomNavigationView) getActivity().findViewById(R.id.navigation);
        navigation.setItemIconTintList(ColorStateList.valueOf(Color.parseColor("#ffEA4C5F")));

        return v;
    }

}
