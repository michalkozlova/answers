package michal.edu.answers;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

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
        Store store = stores.get(position);

        if (convertView == null){
            final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.store_item, parent, false);
        }

        final TextView tvStoreName = convertView.findViewById(R.id.storeName);
        final TextView tvFirstLetter = convertView.findViewById(R.id.firstLetter);
        final CardView cardView = convertView.findViewById(R.id.cardView);

        tvStoreName.setText(store.getStoreNameEng());
        tvFirstLetter.setText(Character.toString(store.getStoreNameEng().charAt(0)));

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.container, new AllBranchesFragment()).addToBackStack("").commit();
            }
        });

        return convertView;
    }
}
