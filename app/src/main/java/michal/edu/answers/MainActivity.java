package michal.edu.answers;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import java.util.ArrayList;
import michal.edu.answers.Models.Store;
import michal.edu.answers.Listeners.StoreListener;

public class MainActivity extends AppCompatActivity {


    private DataSource dataSource = DataSource.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


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

    }



//TODO: addListenerForSingleValueEvent in both projects

}
