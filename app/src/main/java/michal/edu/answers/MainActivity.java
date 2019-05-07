package michal.edu.answers;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.bottomnavigation.LabelVisibilityMode;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import michal.edu.answers.Stores.AllStoresFragment;
import michal.edu.answers.Stores.Store;
import michal.edu.answers.Stores.StoreListener;
import michal.edu.answers.UserDetails.LoginFragment;

public class MainActivity extends AppCompatActivity {


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.my_feedbacks:
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, new MyFeedbackFragment()).commit();
                    return true;
                case R.id.all_stores:
                    new DataSource().getStoresFromFirebase(new StoreListener() {
                        @Override
                        public void onStoreCallBack(ArrayList<Store> stores) {
                            getSupportFragmentManager().beginTransaction().replace(R.id.container, AllStoresFragment.newInstance(stores)).commit();
                        }
                    });
                    return true;
                case R.id.personal_details:
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, new LoginFragment()).commit();
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



        new DataSource().getStoresFromFirebase(new StoreListener() {
            @Override
            public void onStoreCallBack(ArrayList<Store> stores) {
                getSupportFragmentManager().beginTransaction().replace(R.id.container, AllStoresFragment.newInstance(stores)).commit();
            }
        });

        getWindow().setStatusBarColor(Color.parseColor("#ff4954F7"));

    }
}
