package michal.edu.answers;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.bottomnavigation.LabelVisibilityMode;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import michal.edu.answers.Listeners.CustomerListener;
import michal.edu.answers.Models.Customer;
import michal.edu.answers.Models.Feedback;
import michal.edu.answers.Models.Store;
import michal.edu.answers.Listeners.StoreListener;
import michal.edu.answers.UserDetails.LoginFragment;
import michal.edu.answers.UserDetails.PersonalDetailsFragment;

public class MainActivity extends AppCompatActivity {


    private DataSource dataSource = DataSource.getInstance();

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.my_feedbacks:
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.container, new MyFeedbackFragment())
                            .addToBackStack("")
                            .commit();
                    return true;
                case R.id.all_stores:
                    dataSource.getStoresFromFirebase(new StoreListener() {
                        @Override
                        public void onStoreCallBack(ArrayList<Store> stores) {
                            getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.container, AllStoresFragment.newInstance(stores))
                                    .addToBackStack("")
                                    .commit();
                        }
                    });
                    return true;
                case R.id.personal_details:
                    checkIfLoggedIn();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_UNLABELED);
        navigation.setSelectedItemId(R.id.all_stores);
        navigation.setItemIconTintList(ColorStateList.valueOf(Color.parseColor("#ffFEDC32")));

        queryTest();

    }

    private void checkIfLoggedIn(){
        FirebaseAuth.getInstance().addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                boolean isUserLoggedIn = FirebaseAuth.getInstance().getCurrentUser() != null;
                System.out.println(isUserLoggedIn);
                if(!isUserLoggedIn){
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.container, new LoginFragment())
                            .addToBackStack("")
                            .commit();
                }else {
                    String customerID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    dataSource.getCustomerFromFirebase(customerID, new CustomerListener() {
                                @Override
                                public void onCustomerCallback(Customer customer) {
                                    getSupportFragmentManager()
                                            .beginTransaction()
                                            .replace(R.id.container, PersonalDetailsFragment.newInstance(customer))
                                            .addToBackStack("")
                                            .commit();
                                }
                            });
                }
            }
        });
    }



    private void queryTest(){
        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference("Feedbacks")
                .child("WCnPCx747rNWF2bMEXqFWz8Caq63");

        ref.orderByChild("timestamp").startAt(1558471564000L).endAt(1558471680000L).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Feedback feedback = snapshot.getValue(Feedback.class);

                    if (feedback.getCity().equals("Efrat")){
                        System.out.println(feedback.getComment() + ", City: " + feedback.getCity());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
