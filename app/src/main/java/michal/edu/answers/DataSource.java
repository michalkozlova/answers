package michal.edu.answers;

import android.content.Context;
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
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import michal.edu.answers.Models.Branch;
import michal.edu.answers.Branch.BranchListener;
import michal.edu.answers.Models.Question;
import michal.edu.answers.Models.Section;
import michal.edu.answers.Models.Store;
import michal.edu.answers.Stores.StoreListener;

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



    public ArrayList<Branch> getBranchesFromFirebase(Store store, final BranchListener callback){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Stores").child(store.getStoreID()).child("Branches");
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
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return mBranches;
    }


    public ArrayList<Section> getQuestionnaireFromFirebase(final Store store, final SectionListener callback){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Stores").child(store.getStoreID()).child("questionnaire");
        final ArrayList<Section> mQuestionnaire = new ArrayList<>();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    final Section value = snapshot.getValue(Section.class);
                    //TODO: WTF??!!
                    value.getQuestions().remove(0);
                    mQuestionnaire.add(value);
                }

                if(mQuestionnaire.isEmpty()){
                    System.out.println("no questionnaire");
                }else{
                    callback.onSectionCallback(mQuestionnaire);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return mQuestionnaire;
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

}



