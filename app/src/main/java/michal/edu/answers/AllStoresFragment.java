package michal.edu.answers;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class AllStoresFragment extends Fragment {

    private StoreAdapter adapter;
    private GridView gvStores;

    public static AllStoresFragment newInstance(ArrayList<Store> stores) {

        Bundle args = new Bundle();
        args.putSerializable("stores", stores);
        AllStoresFragment fragment = new AllStoresFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_all_stores, container, false);
        gvStores = v.findViewById(R.id.gvStores);

        ArrayList<Store> stores = (ArrayList<Store>) getArguments().getSerializable("stores");
        adapter = new StoreAdapter(stores, getActivity());
        //gvStores.setLayoutManager(new LinearLayoutManager(getContext()));
        gvStores.setAdapter(adapter);

        return v;
    }

}
