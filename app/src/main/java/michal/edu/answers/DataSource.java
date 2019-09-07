package michal.edu.answers;

import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Cache;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import michal.edu.answers.Listeners.CustomerListener;
import michal.edu.answers.Listeners.SectionListener;
import michal.edu.answers.Models.Branch;
import michal.edu.answers.Listeners.BranchListener;
import michal.edu.answers.Models.Customer;
import michal.edu.answers.Models.Section;
import michal.edu.answers.Models.Store;
import michal.edu.answers.Listeners.StoreListener;

public class DataSource {

    private static DataSource dataSource;

    public static DataSource getInstance(){
        if (dataSource == null){
            dataSource = new DataSource();
        }

        return dataSource;
    }


    public ArrayList<Store> getStoresFromFirebase(final StoreListener callback){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Stores");
        final ArrayList<Store> mStores = new ArrayList<>();
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    final Store value = snapshot.getValue(Store.class);

                    if (value.getHasBranches()){
                        mStores.add(value);
                    }


//                    getBranchesFromFirebase(value, new BranchListener() {
//                        @Override
//                        public void onBranchCallback(ArrayList<Branch> branches) {
//                            if (branches.isEmpty()){
//                                System.out.println("no branches for " + value.getStoreName());
//                            }else {
//                                mStores.add(value);
//                            }
//                        }
//                    });

                }


                if (mStores.isEmpty()){
                    System.out.println("no stores");
                } else {
                    callback.onStoreCallBack(mStores);
                    System.out.println("stores: " + mStores);
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println(databaseError);
            }
        });

        return mStores;
    }

    public ArrayList<Branch> getBranchesFromFirebase(Store store, final BranchListener callback){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Stores").child(store.getStoreID()).child("branches");
        final ArrayList<Branch> mBranches = new ArrayList<>();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Branch value = snapshot.getValue(Branch.class);
                    mBranches.add(value);
                }

                if (mBranches.isEmpty()){
                    callback.onBranchCallback(mBranches);
                    System.out.println("no branches");
                }else {
                    callback.onBranchCallback(mBranches);
                    System.out.println(mBranches);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return mBranches;
    }


    public void setStoreLogo(final Store store, final ImageView imageView, final TextView firstLetter, final Context context){
        StorageReference storageRef = MyImageStorage.getInstance();

        storageRef.child(store.getStoreName()+".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(context).load(uri).into(imageView);
                firstLetter.setText("");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                firstLetter.setText(Character.toString(store.getStoreName().charAt(0)));
            }
        });
    }


    public void getCustomerFromFirebase(final String customerID, final CustomerListener callback){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Customers").child(customerID);
        final ArrayList<Customer> mCustomers = new ArrayList<>();
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                HashMap<String, Object> map = dataSnapshot.getValue(new GenericTypeIndicator<HashMap<String, Object>>() {
                });

                String customerName = (String) map.get("customerName");
                String customerPhone = (String) map.get("customerPhone");

                Customer customer = new Customer(customerID, customerName, customerPhone);
                mCustomers.add(customer);

                callback.onCustomerCallback(mCustomers.get(0));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println(databaseError);
            }
        });
    }

}



