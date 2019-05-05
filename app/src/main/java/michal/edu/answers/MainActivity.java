package michal.edu.answers;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:

                    return true;
                case R.id.navigation_dashboard:
                    getStoresFromFirebase(new StoreListener() {
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
}
