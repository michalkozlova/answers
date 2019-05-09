package michal.edu.answers;


import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.ArrayList;

import michal.edu.answers.Adapters.StoreAdapter;
import michal.edu.answers.Models.Store;


/**
 * A simple {@link Fragment} subclass.
 */
public class AllStoresFragment extends Fragment {

    private StoreAdapter adapter;
    private GridView gvStores;
    private TabLayout tablayout;
    private ArrayList<Store> allStores, stores, restaurants;

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
        tablayout = v.findViewById(R.id.tablayout);

        tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0){
                    adapter = new StoreAdapter(allStores, getActivity());
                    gvStores.setAdapter(adapter);
                } else if (tab.getPosition() == 1){
                    adapter = new StoreAdapter(stores, getActivity());
                    gvStores.setAdapter(adapter);
                } else{
                    adapter = new StoreAdapter(restaurants, getActivity());
                    gvStores.setAdapter(adapter);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        getAllLists();
        adapter = new StoreAdapter(allStores, getActivity());
        gvStores.setAdapter(adapter);

        getActivity().getWindow().setStatusBarColor(Color.parseColor("#ff4954F7"));
        BottomNavigationView navigation = (BottomNavigationView) getActivity().findViewById(R.id.navigation);
        navigation.setItemIconTintList(ColorStateList.valueOf(Color.parseColor("#ffFEDC32")));


        View bottomBar = getActivity().getWindow().findViewById(R.id.navigation);
        //bottomBar.clearAnimation();
        bottomBar.animate().translationY(0).setDuration(400);

        return v;
    }


    private void getAllLists(){
        allStores = (ArrayList<Store>) getArguments().getSerializable("stores");
        stores = new ArrayList<>();
        restaurants = new ArrayList<>();
        for (Store store : allStores) {
            if (store.getStoreType() == 0){
                stores.add(store);
            } else {
                restaurants.add(store);
            }
        }
    }
}
