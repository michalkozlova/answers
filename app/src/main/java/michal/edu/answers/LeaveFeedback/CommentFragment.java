package michal.edu.answers.LeaveFeedback;


import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import michal.edu.answers.AllStoresFragment;
import michal.edu.answers.DataSource;
import michal.edu.answers.Listeners.StoreListener;
import michal.edu.answers.Models.Store;
import michal.edu.answers.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CommentFragment extends Fragment {

    private DataSource dataSource = DataSource.getInstance();
    private ProgressBar progressBar;
    private TextView firstLetter;
    private ImageView cardImage;
    private Button btnDone;

    public static CommentFragment newInstance(Store store, String branchName) {

        Bundle args = new Bundle();
        args.putSerializable("store", store);
        args.putSerializable("branchName", branchName);
        CommentFragment fragment = new CommentFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_comment, container, false);

        progressBar = v.findViewById(R.id.progressBar);
        firstLetter = v.findViewById(R.id.firstLetter);
        cardImage = v.findViewById(R.id.cardImage);
        btnDone = v.findViewById(R.id.btnDone);

        final Store thisStore = (Store) getArguments().getSerializable("store");
        final String thisBranchName = (String) getArguments().getSerializable("branchName");

        dataSource.setStoreLogo(thisStore, cardImage, firstLetter, getContext());

        progressBar.setProgress(5);

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doneDialog();
                dataSource.getStoresFromFirebase(new StoreListener() {
                    @Override
                    public void onStoreCallBack(ArrayList<Store> stores) {
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, AllStoresFragment.newInstance(stores)).commit();
                    }
                });
            }
        });

        return v;
    }


    private void doneDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setTitle("Thank you!");
        //TODO: different message depends on feedback result
        builder.setMessage("We really appreciate your time");
        builder.show();
    }

}
