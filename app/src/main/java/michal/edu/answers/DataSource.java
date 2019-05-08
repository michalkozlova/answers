package michal.edu.answers;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import michal.edu.answers.Branch.Branch;
import michal.edu.answers.Branch.BranchListener;
import michal.edu.answers.Stores.Store;
import michal.edu.answers.Stores.StoreListener;

public class DataSource {

    public ArrayList<Store> getStoresFromFirebase(final StoreListener callback){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Stores");
        final ArrayList<Store> mStores = new ArrayList<>();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Store value = snapshot.getValue(Store.class);
                    mStores.add(value);
                }

                if (mStores.isEmpty()){
                    System.out.println("no stores");
                } else {
                    callback.onStoreCallBack(mStores);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return mStores;
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



