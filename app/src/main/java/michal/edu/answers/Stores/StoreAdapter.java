package michal.edu.answers.Stores;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

import michal.edu.answers.Branch.AllBranchesFragment;
import michal.edu.answers.MyImageStorage;
import michal.edu.answers.R;
import michal.edu.answers.Models.Store;

public class StoreAdapter extends BaseAdapter {

    private List<Store> stores;
    private FragmentActivity activity;

    public StoreAdapter(List<Store> stores, FragmentActivity activity) {
        this.stores = stores;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return stores.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Store store = stores.get(position);

        if (convertView == null){
            final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.item_store, parent, false);
        }

        final TextView tvStoreName = convertView.findViewById(R.id.storeName);
        final TextView tvFirstLetter = convertView.findViewById(R.id.firstLetter);
        final CardView cardView = convertView.findViewById(R.id.cardView);
        final ImageView cardImage = convertView.findViewById(R.id.cardImage);

        tvStoreName.setText(store.getStoreName());

        StorageReference storageRef = MyImageStorage.getInstance();

        storageRef.child(store.getStoreName()+".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(activity).load(uri).into(cardImage);
                tvFirstLetter.setText("");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                tvFirstLetter.setText(Character.toString(store.getStoreName().charAt(0)));
            }
        });

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.container, AllBranchesFragment.newInstance(store)).addToBackStack("").commit();
            }
        });

        return convertView;
    }
}
